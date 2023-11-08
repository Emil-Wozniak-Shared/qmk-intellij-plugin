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
import pl.ejdev.qmk.model.KeyboardCap
import pl.ejdev.qmk.model.KeyboardInfo
import pl.ejdev.qmk.service.SupportedKeyboardsService
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import pl.ejdev.qmk.window.components.*
import javax.swing.Box

private const val KEYCODES_PATH = "keycodes/keycodes.csv"
private const val KEYBOARD_INDEX = 0

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val keyboardCache: List<KeyboardInfo> = SupportedKeyboardsService.loadAllSupportedKeyboards()
    private val layoutNames = keyboardCache.map { it.keyboard to it.layouts }
    private val keyCodes = KeyCodeParser.parse(IntellijIdeaResourceLoader.getResource(KEYCODES_PATH))
    private val windowState: WindowState = WindowState()

    init {
        windowState.lines = keyboardFileLines()
        windowState.caps = loadKeyboardCaps()
    }

    private lateinit var filePathsComboBox: Cell<ComboBox<String>>
    private lateinit var keyboardCell: Cell<Box>

    val content = panel {
        row {
            filePathsComboBox = comboBox(keyboardCache.map(KeyboardInfo::keyboard)).onChange { selected ->
                refreshState(selected.toString())
                refreshKeyboard()
            }
        }
        row {
            cell(vbox { uploadFilePanel(action = ::importConfig) })
        }
        row {
            keyboardCell = cell(createKeyCaps(windowState.caps, defaultLayout, keyCodes).addToKeyboardBox())
        }
    }

    private fun List<KeyCaps>.addToKeyboardBox(): Box =
        Box.createVerticalBox().also { keyboard -> this.forEach { keyboardCap -> keyboard.add(keyboardCap) } }

    private fun keyboardFileLines(): List<String> =
        IntellijIdeaResourceLoader.getResource("keyboards/${windowState.filePath}/info.json")

    private fun loadKeyboardCaps(): List<KeyboardCap> =
        KeyboardLoader
            .load(windowState.lines, windowState.layoutName)
            ?.caps
            .orEmpty()

    private fun refreshState(selected: String) {
        windowState.apply {
            filePath = selected
            lines = keyboardFileLines()
            layoutName = layoutNames.find { it.first == selected }!!.second.first()
            caps = loadKeyboardCaps()
        }
    }

    private fun importConfig(content: String): List<List<String>> =
        KeyboardLoader.parseConfigFile(content)
            .also { windowState.layoutConfig = it }
            .also { refreshKeyboard() }

    private fun refreshKeyboard() {
        keyboardCell.applyToComponent {
            remove(KEYBOARD_INDEX)
            repaint()
            add(createKeyCaps(windowState.caps, windowState.layoutConfig, keyCodes).addToKeyboardBox())
        }
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}


