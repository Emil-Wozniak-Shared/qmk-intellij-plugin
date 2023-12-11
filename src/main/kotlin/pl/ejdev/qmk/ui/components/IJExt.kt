package pl.ejdev.qmk.ui.components

import com.intellij.ui.components.JBBox
import com.intellij.ui.dsl.builder.Row

fun JBBox.column(ctx: JBBox.() -> Unit) {
    add(hbox(ctx))
}

fun Row.column(ctx: JBBox.() -> Unit) {
    cell(hbox(ctx))
}

fun Row.innerRow(ctx: JBBox.() -> Unit) {
    cell(vbox(ctx))
}