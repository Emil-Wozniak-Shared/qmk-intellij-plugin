package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.dsl.builder.Cell

fun <T: Any> Cell<ComboBox<T>>.onChange(action: (Any) -> Unit) = apply{
    applyToComponent {
        addActionListener { event ->
            if (event.actionCommand == "comboBoxChanged") {
                action(requireNotNull((event.source as ComboBox<*>).selectedItem))
            }
        }
    }
}