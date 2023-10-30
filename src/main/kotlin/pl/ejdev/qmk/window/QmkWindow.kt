@file:Suppress("UnstableApiUsage")

package pl.ejdev.qmk.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.JBColor
import com.intellij.ui.RoundedLineBorder
import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.qmk.KeyboardLoader
import pl.ejdev.qmk.model.KeyboardCap
import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import pl.ejdev.qmk.window.components.*
import java.awt.Dimension

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    val content = panel {
        val filePath = "keyboards/ergodox_ez/info.json"
        val lines = IntellijIdeaResourceLoader.getResource(filePath)
        val caps: List<KeyboardCap> = KeyboardLoader.load(lines, layoutName = "LAYOUT_ergodox")
            ?.caps
            .orEmpty()

        keyboardLayout(caps)

    }


    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}



