package pl.ejdev.qmk.utils.listeners

import com.intellij.ui.components.JBTextField
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

internal fun JBTextField.onTextChange(action: (String) -> Unit) = apply {
    val listener = OnChangeListener(this, action)
    this.document.addDocumentListener(listener)
}

private class OnChangeListener(private val target: JTextField, val onChange: (String) -> Unit) : DocumentListener {
    override fun insertUpdate(e: DocumentEvent?) {
        updateFieldState()
    }

    override fun removeUpdate(e: DocumentEvent?) {
        updateFieldState()
    }

    override fun changedUpdate(e: DocumentEvent?) {
        updateFieldState()
    }

    private fun updateFieldState() {
        val text: String = target.getText()
        onChange(text)
    }
}