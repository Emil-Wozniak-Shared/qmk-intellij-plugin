package pl.ejdev.qmk.window.components

import com.intellij.ui.components.JBBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row

internal fun Row.layerSwitcher(layersSize: Int, switchLayer: (index: Int ) -> Unit): Cell<JBBox> =
    hbox box@ {
        List(layersSize) { index ->
            jButton("$index") {
                this.addActionListener {
                    val layerIndex = it.actionCommand.toInt()
                    switchLayer(layerIndex)
                }
            }
        }.forEach { jButton -> this@box + jButton }
    }.let(::cell)
