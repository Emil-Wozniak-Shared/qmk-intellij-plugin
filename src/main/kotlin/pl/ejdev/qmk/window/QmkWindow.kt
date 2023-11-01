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
import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import pl.ejdev.qmk.window.components.KeyCaps
import pl.ejdev.qmk.window.components.createKeyCaps
import pl.ejdev.qmk.window.components.onChange
import javax.swing.Box

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val keyCodes = KeyCodeParser.parse(IntellijIdeaResourceLoader.getResource("keycodes/keycodes.csv"))

    private val filePaths = listOf(
        "keyboards/ergodox_ez",
        "keyboards/handwired/dactyl_manuform/5x6_6"
    )
    private val layoutNames = listOf(
        "LAYOUT_ergodox",
        "LAYOUT_split_5x6_6"
    )

    private lateinit var filePathsComboBox: Cell<ComboBox<String>>
    private lateinit var kbCell: Cell<Box>

    @Suppress("PrivatePropertyName")
    private val DEFAULT_INDEX = 0

    val content = panel {
        var filePath = filePaths[DEFAULT_INDEX]
        var layoutName = layoutNames[DEFAULT_INDEX]
        var lines = getInfoJSONLines(filePath)
        var caps: List<KeyboardCap> = keyboardCaps(lines, layoutName)

        row {
            filePathsComboBox = comboBox(filePaths).onChange { selected ->
                filePath = selected.toString()
                lines = getInfoJSONLines(filePath)
                layoutName = when {
                    filePath == filePaths[1] -> layoutNames[1]
                    else -> layoutNames.first()
                }
                caps = keyboardCaps(lines, layoutName)
                kbCell.apply {
                    component.remove(0)
                    component.repaint()
                    component.add(createKeyCaps(caps, qmkLayout, keyCodes).addToKeyboardBox())
                }
            }
        }

        row {
            kbCell = cell(createKeyCaps(caps, qmkLayout, keyCodes).addToKeyboardBox())
        }
    }

    private fun List<KeyCaps>.addToKeyboardBox(): Box =
        Box.createVerticalBox().also { keyboard -> this.forEach { keyboardCap -> keyboard.add(keyboardCap) } }

    private fun getInfoJSONLines(filePath: String): List<String> =
        IntellijIdeaResourceLoader.getResource("$filePath/info.json")

    private fun keyboardCaps(lines: List<String>, layoutName: String): List<KeyboardCap> =
        KeyboardLoader
            .load(lines, layoutName = layoutName)
            ?.caps
            .orEmpty()

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}


