package pl.ejdev.qmk.window.components

import com.intellij.BundleBase
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBBox
import com.intellij.ui.components.JBPanel
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import javax.swing.JButton

class LayerSwitcher(
    private val layersSize: Int,
    private val switchLayer: (index: Int) -> Unit
) : JBPanel<DialogPanel>(FlowLayout()) {
    init {
        add(JBBox.createHorizontalBox().apply {
            List(layersSize) { index ->
                button("$index") {
                    val layerIndex = it.actionCommand.toInt()
                    switchLayer(layerIndex)
                }
            }.forEach(this::add)
        })
    }

    private fun button(text: String, actionListener: (event: ActionEvent) -> Unit) =
        JButton(BundleBase.replaceMnemonicAmpersand(text)).apply {
            addActionListener(actionListener)
        }
}