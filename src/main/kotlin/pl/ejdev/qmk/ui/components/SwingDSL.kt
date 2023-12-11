package pl.ejdev.qmk.ui.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBBox
import java.awt.Component
import java.awt.Dimension
import java.io.File
import javax.swing.*

fun <T : Any> ComboBox<T>.onChange(action: (Any) -> Unit) = apply {
    addActionListener { event ->
        if (event.actionCommand == "comboBoxChanged") {
            when (val item = (event.source as ComboBox<*>).selectedItem) {
                null -> {}
                else -> action(item)
            }
        }
    }
}

fun jButton(name: String, ctx: JButton.() -> Unit) = JButton(name).apply(ctx)

fun textField(ctx: JTextField.() -> Unit) = JTextField().apply(ctx)


fun setupPanel(
    prefWidth: Int,
    prefHeight: Int,
    ctx: JPanel.() -> Unit
) = object : JPanel() {
    override fun getPreferredSize(): Dimension = Dimension(prefWidth, prefHeight)
}.apply(ctx)

fun label(text: String, ctx: JLabel.() -> Unit) = JLabel(text).apply(ctx)

fun fileChooser(parent: JComponent? = null, onSelect: JFileChooser.(File) -> Unit) = JFileChooser()
    .apply { showOpenDialog(parent ?: this) }
    .apply {
        if (selectedFile != null) {
            onSelect(selectedFile)
        }
    }

fun vbox(ctx: JBBox.() -> Unit): JBBox = JBBox.createVerticalBox().apply(ctx)
fun hbox(ctx: JBBox.() -> Unit): JBBox = JBBox.createHorizontalBox().apply(ctx)

operator fun JComponent.plus(component: JComponent) {
    this.add(component)
}

class BoundryDsl(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
)

fun Component.bounds(dsl: BoundryDsl.() -> Unit) {
    BoundryDsl().apply(dsl).let {
        this.setBounds(it.x  ,it.y,it.width, it.height)
    }
}
