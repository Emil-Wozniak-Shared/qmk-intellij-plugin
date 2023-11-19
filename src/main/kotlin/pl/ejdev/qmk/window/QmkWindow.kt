@file:Suppress("UnstableApiUsage")

package pl.ejdev.qmk.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.qmk.KeyboardLoader
import pl.ejdev.qmk.keycodes.KeyCodeParser
import pl.ejdev.qmk.service.LayoutService
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import pl.ejdev.qmk.utils.listeners.addKeyPressListener
import pl.ejdev.qmk.window.components.createKeyCaps
import pl.ejdev.qmk.window.components.onChange
import pl.ejdev.qmk.window.components.uploadFilePanel
import pl.ejdev.qmk.window.components.vbox
import pl.ejdev.qmk.window.ui.TIME_NEW_ROMAN_18
import java.awt.event.KeyEvent.VK_BACK_SPACE
import javax.swing.Box
import javax.swing.JPanel
import kotlin.concurrent.thread

private const val KEYCODES_PATH = "keycodes/keycodes.csv"
private const val KEYBOARD_INDEX = 0

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val keyCodes =
        KeyCodeParser.parse(IntellijIdeaResourceLoader.getResource(KEYCODES_PATH).getOrNull().orEmpty())
    private val windowState: WindowState = WindowState()
    private var error: String = ""

    init {
        LayoutService.fromHomeDir().let { windowState.layouts = it }
        keyboardFileLines { windowState.lines = it }
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
            textField().apply {
                var text = component.text
                component.addKeyPressListener(
                    press = {
                        text += it.keyChar
                        filePathsComboBox.apply {
                            thread { findKeyboardInfo(text) }
                        }
                    },
                    release = {
                        if (it.keyCode == VK_BACK_SPACE) {
                            text = text.substring(0, text.length - 2)
                            filePathsComboBox.apply {
                                thread { findKeyboardInfo(text) }
                            }
                        }
                    }
                )
            }
            filePathsComboBox = comboBox(windowState.layouts.map { it.filename })
                .onChange { selected ->
                    refreshState(keyboardName = selected.toString())
                    refreshKeyboard()
                }
        }
        row { cell(vbox { uploadFilePanel(action = ::importConfig) }) }
        row {
            keyboardCell = cell(
                createKeyCaps(
                    defaultLayout,
                    keyCodes,
                    windowState
                ).addToKeyboardBox()
            )
        }
    }

    private fun Cell<ComboBox<String>>.findKeyboardInfo(text: String) {
        keyboardFileLines {
            windowState.lines = it.filter { keyboard -> keyboard.contains(text) }
        }.onSuccess {
            this.component.removeAllItems()
            it.filter { keyboard -> keyboard.contains(text) }.forEach { component.addItem(it) }
        }
    }

    private fun List<JPanel>.addToKeyboardBox(): Box =
        Box.createVerticalBox().also { keyboard -> this.forEach { keyboardCap -> keyboard.add(keyboardCap) } }

    private fun keyboardFileLines(call: (List<String>) -> Unit): Result<List<String>> =
        if (windowState.keyboard.isEmpty()) {
            error = "No keyboard set"
            Result.success(emptyList())
        } else IntellijIdeaResourceLoader
            .getResource("keyboards/${windowState.keyboard}/info.json")
            .onSuccess(call)
            .onFailure { error = "${it.message}" }

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

    private fun importConfig(content: String): List<List<String>> =
        KeyboardLoader.parseConfigFile(content).also {
            windowState.layers = it
            refreshKeyboard()
        }

    private fun refreshKeyboard() {
        keyboardCell.applyToComponent {
            remove(KEYBOARD_INDEX)
            repaint()
            add(createKeyCaps(windowState.layers, keyCodes, windowState).addToKeyboardBox())
        }
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}
