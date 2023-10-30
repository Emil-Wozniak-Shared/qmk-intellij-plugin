package pl.ejdev.qmk.window.components

import com.intellij.ui.components.JBBox
import com.intellij.ui.dsl.builder.Row
import java.awt.Component
import javax.swing.Box
import javax.swing.BoxLayout.X_AXIS
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JComponent

var JComponent.children: List<Component>
    get() {
        return this.components.toList()
    }
    set(value) {
        value?.forEach { add(it) }
    }

var JComponent.child: Component?
    get() {
       return this.components.firstOrNull()
    }
    set(value) {
        add(value)
    }

operator fun JComponent.plusAssign(components: List<JComponent>)  {
    components.forEach(this::add)
}

fun Row.column(children: List<JComponent>): Box {
    return Box.createVerticalBox().apply {
        children.forEach(::add)
    }.also(::cell)
}

fun Row.column(dsl: Box.() -> Unit): Box = Box.createVerticalBox()
    .apply(dsl)
    .also(::cell)

fun JComponent.box(y: Boolean = false, dsl: JBBox.() -> Unit) = JBBox(if (y) Y_AXIS else X_AXIS)
    .apply(dsl)