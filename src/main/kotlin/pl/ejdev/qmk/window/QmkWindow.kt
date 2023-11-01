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
import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import pl.ejdev.qmk.window.components.KeyCaps
import pl.ejdev.qmk.window.components.createKeyCaps
import pl.ejdev.qmk.window.components.onChange
import java.nio.file.Path
import javax.swing.Box
import kotlin.io.path.pathString

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private val keyCodes = KeyCodeParser.parse(IntellijIdeaResourceLoader.getResource("keycodes/keycodes.csv"))
    private val keyboardCache: List<KeyboardInfo> = loadAllSupportedKeyboards()

    private val filePaths = keyboardCache.map { it.keyboard }
    private val layoutNames = keyboardCache.map { it.keyboard to it.layouts }

    private lateinit var filePathsComboBox: Cell<ComboBox<String>>
    private lateinit var kbCell: Cell<Box>

    val content = panel {
        var filePath = "ergodox_ez"
        var layoutName = "LAYOUT_ergodox"
        var lines = keyboardFileLines(filePath)
        var caps: List<KeyboardCap> = keyboardCaps(lines, layoutName)

        row {
            filePathsComboBox = comboBox(filePaths).onChange { selected ->
                filePath = selected.toString()
                lines = keyboardFileLines(filePath)
                layoutName = layoutNames.find { it.first == selected }!!.second.first()
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

    private fun keyboardFileLines(filePath: String): List<String> =
        IntellijIdeaResourceLoader.getResource("keyboards/$filePath/info.json")

    private fun keyboardCaps(lines: List<String>, layoutName: String): List<KeyboardCap> =
        KeyboardLoader
            .load(lines, layoutName = layoutName)
            ?.caps
            .orEmpty()

    private fun loadAllSupportedKeyboards(): List<KeyboardInfo> {
        val infoFile = "info.json"
        return IntellijIdeaResourceLoader.listDir("keyboards")
            .asSequence()
            .filter { it.endsWith(infoFile) }
            .map(Path::pathString)
            .map {
                it
                    .replace("keyboards/", "")
                    .replace("/$infoFile", "")
            }
            .toList()
            .let { keyboards ->
                keyboards
                    .mapNotNull {
                        val lines = IntellijIdeaResourceLoader.getResource("keyboards/$it/$infoFile")
                        KeyboardLoader.extractKeyboardInfo(it, lines)
                    }
            }
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}


