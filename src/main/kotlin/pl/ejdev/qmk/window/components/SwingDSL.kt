package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBBox
import com.intellij.ui.dsl.builder.Cell
import pl.ejdev.qmk.window.ui.TIME_NEW_ROMAN_18
import java.awt.Dimension
import java.awt.Font
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

fun button(name: String, ctx: JButton.() -> Unit) = JButton(name).apply(ctx)

fun textField(ctx: JTextField.() -> Unit) = JTextField().apply(ctx)

fun jlabel(text: String, font: Font? = null) = JLabel(text).apply {
    font?.let { this.font = it }
}

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

