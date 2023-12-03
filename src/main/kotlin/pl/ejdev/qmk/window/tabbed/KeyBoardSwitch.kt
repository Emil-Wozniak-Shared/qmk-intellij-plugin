package pl.ejdev.qmk.window.tabbed

import com.intellij.ide.ui.LafManager
import com.intellij.ide.ui.UISettings
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.components.JBBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanel
import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.service.KeyboardService
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCode
import pl.ejdev.qmk.window.components.*
import pl.ejdev.qmk.window.defaultLayout

internal class KeyBoardSwitch(
    private val windowState: WindowState,
    private val settingsManager: UISettings,
    private val keyCodes: List<KeyCode>,
) : JBPanel<DialogPanel>(VerticalFlowLayout()) {
    private companion object {
        private const val KEYBOARD_INDEX = 0
    }

    private lateinit var keyboard: KeyCapsPanel
    private lateinit var layersSwitchButtons: JBBox

    init {
        add(JBBox.createHorizontalBox().let { comboBox ->
            JBBox.createHorizontalBox().apply {
                add(JBLabel("Select keyboard"))
                add(
                    ComboBox((windowState.keyboards.map { it.filename }).toTypedArray())
                        .onChange { selected ->
                            refreshState(selected.toString())
                            refreshKeyboard()
                        })
            }.let { comboBox.add(it) }
        })
        add(JBBox.createHorizontalBox().let { switchBox ->
            layerSwitcher(windowState.layers.size, ::layerSwitch)
                .also { layersSwitchButtons = it }
                .let { switchBox.add(it) }
        })
        add(JBBox.createHorizontalBox().let { keyboardPanelBox ->
            KeyCapsPanel(defaultLayout, keyCodes, windowState, settingsManager.fontSize)
                .also { keyboard = it }
                .let { keyboardPanelBox.add(it) }
        })
    }

    fun refreshKeyboard() {
        keyboard.apply {
            remove(KEYBOARD_INDEX)
            repaint()
            this + KeyCapsPanel(windowState.layers, keyCodes, windowState, settingsManager.fontSize)
        }
        layersSwitchButtons.repaint()

        LafManager.getInstance().updateUI()
    }

    private fun refreshState(keyboard: String) {
        windowState.apply {
            this.change(keyboard = keyboard, setLayout = KeyboardService::setActiveKeyboard)
        }
    }

    private fun layerSwitch(layerIndex: Int) {
        keyboard.apply {
            this.layers.mapIndexed { index, layer ->
                layer.isVisible = index == layerIndex
                layer.repaint()
            }
        }
    }

    fun addToIJ(row: Row) = row.cell(this)
}