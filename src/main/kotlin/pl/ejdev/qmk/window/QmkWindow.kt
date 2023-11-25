@file:Suppress("UnstableApiUsage")

package pl.ejdev.qmk.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.qmk.service.KeyboardService
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCodeLoader
import pl.ejdev.qmk.window.components.*
import pl.ejdev.qmk.window.ui.TIME_NEW_ROMAN_18
import javax.swing.JSeparator

private const val KEYBOARD_INDEX = 0

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val keyCodes = KeyCodeLoader.load()
    private val windowState: WindowState = WindowState()
    private var error: String = ""

    init {
        KeyboardService.fromHomeDir().let { windowState.keyboards = it }
    }

    private lateinit var filePathsComboBox: ComboBox<String>
    private lateinit var keyboardCell: Cell<KeyCapsPanel>
    private lateinit var layersSwitchButtons: Cell<JBBox>

    val content = panel {
        row { errorLabel(error) }
        row { cell(JSeparator()) }
        row {
            innerRow {
                this + jlabel("Select keyboard", TIME_NEW_ROMAN_18)
                filePathsComboBox = keyboardLayoutsComboBox(windowState, ::refreshState, ::refreshKeyboard)
            }
            innerRow {
                this + jlabel("Find keyboard", TIME_NEW_ROMAN_18)
                column {
                    ghButton()
                    searchTextField()
                }
            }
        }
        row { cell(JSeparator()) }
        row { label("Upload layers").applyToComponent { font = TIME_NEW_ROMAN_18 } }
        row { uploadLayersBox(windowState, ::refreshKeyboard) }
        row {
            label("Switch layer").applyToComponent { font = TIME_NEW_ROMAN_18 }
            layersSwitchButtons = layerSwitcher(windowState.layers.size, ::layerSwitch)
        }
        row { keyboardCell = cell(keyCapsPanel(defaultLayout, keyCodes, windowState)) }
    }

    private fun refreshState(keyboard: String) {
        windowState.apply {
            this.change(keyboard = keyboard, setLayout = KeyboardService::setActiveKeyboard)
        }
    }

    private fun refreshKeyboard() {
        keyboardCell.applyToComponent {
            remove(KEYBOARD_INDEX)
            repaint()
            this + keyCapsPanel(windowState.layers, keyCodes, windowState)
        }
    }

    private fun layerSwitch(layerIndex: Int) {
        keyboardCell.applyToComponent {
            this.layers.mapIndexed { index, layer ->
                layer.isVisible = index == layerIndex
                layer.repaint()
            }
        }
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}




