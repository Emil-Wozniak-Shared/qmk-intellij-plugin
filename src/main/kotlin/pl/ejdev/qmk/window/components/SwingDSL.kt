package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.dsl.builder.Cell
import java.awt.Dimension
import javax.swing.*

fun <T : Any> Cell<ComboBox<T>>.onChange(action: (Any) -> Unit) = apply {
    applyToComponent {
        addActionListener { event ->
            if (event.actionCommand == "comboBoxChanged") {
                action(requireNotNull((event.source as ComboBox<*>).selectedItem))
            }
        }
    }
}

fun button(name: String, ctx: JButton.() -> Unit) = JButton(name).apply(ctx)

fun textField(ctx: JTextField.() -> Unit) = JTextField().apply(ctx)

fun setupPanel(
    prefWidth: Int,
    prefHeight: Int,
    ctx: JPanel.() -> Unit
) = object : JPanel() {
    override fun getPreferredSize(): Dimension = Dimension(prefWidth, prefHeight)
}.apply(ctx)

fun label(text: String, ctx: JLabel.() -> Unit) = JLabel(text).apply(ctx)

operator fun JComponent.plus(component: JComponent) {
    this.add(component)
}