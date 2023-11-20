@file:Suppress("UnstableApiUsage")

package pl.ejdev.qmk.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.qmk.service.LayoutService
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCodeLoader
import pl.ejdev.qmk.window.components.keyCapsPanel
import pl.ejdev.qmk.window.components.keyboardLayoutsComboBox
import pl.ejdev.qmk.window.components.searchTextField
import pl.ejdev.qmk.window.components.uploadLayersBox
import pl.ejdev.qmk.window.ui.TIME_NEW_ROMAN_18
import javax.swing.Box

private const val KEYBOARD_INDEX = 0

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val keyCodes = KeyCodeLoader.load()
    private val windowState: WindowState = WindowState()
    private var error: String = ""

    init {
        LayoutService.fromHomeDir().let { windowState.layouts = it }
    }

    private lateinit var filePathsComboBox: Cell<ComboBox<String>>
    private lateinit var keyboardCell: Cell<Box>

    val content = panel {
        row {
            label(error).applyToComponent {
                font = TIME_NEW_ROMAN_18
                text = error
            }
        }
        row {
            filePathsComboBox = keyboardLayoutsComboBox(windowState, ::refreshState, ::refreshKeyboard)
            searchTextField(filePathsComboBox)
        }
        row { uploadLayersBox(windowState, ::refreshKeyboard) }
        row {
            keyboardCell = cell(keyCapsPanel(defaultLayout, keyCodes, windowState))
        }
    }

    private fun refreshState(keyboardName: String) {
        windowState.apply {
            layouts = layouts.map {
                if (it.filename == keyboardName) {
                    LayoutService.setActiveLayout(it)
                    it.copy(
                        active = true,
                        layouts = it.layouts.mapIndexed { index, layout -> layout.copy(active = index == 0) }
                    )
                } else it.copy(active = false, layouts = it.layouts.map { it.copy(active = false) })
            }
            keyboard = keyboardName
        }
    }

    private fun refreshKeyboard() {
        keyboardCell.applyToComponent {
            remove(KEYBOARD_INDEX)
            repaint()
            add(keyCapsPanel(windowState.layers, keyCodes, windowState))
        }
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}
