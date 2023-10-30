package pl.ejdev.qmk.window.components

import com.intellij.ui.JBColor
import com.intellij.ui.RoundedLineBorder
import com.intellij.ui.components.JBLabel
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.model.KeyboardCap
import java.awt.Dimension

fun Panel. keyboardLayout(caps: List<KeyboardCap>) {
    val right = caps.subList(0, caps.size / 2).groupBy { it.matrix!![1] }
    val left = caps.subList(caps.size / 2, caps.size - 1).groupBy { it.matrix!![1] }

    row {
        right.values.map(::caps)
        left.values.map(::caps)
    }
}

private fun Row.caps(caps: List<KeyboardCap>) =
    column {
        children = caps.map {
            box {
                border = RoundedLineBorder(JBColor.GRAY)
                size = Dimension(3, 1)
                child = JBLabel("""${it.matrix}""")
            }
        }
    }