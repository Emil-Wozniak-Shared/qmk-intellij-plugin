package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBBox
import com.intellij.ui.components.JBTextField
import pl.ejdev.qmk.utils.listeners.addKeyPressListener
import java.awt.event.KeyEvent
import kotlin.concurrent.thread

fun JBBox.searchTextField() {
    JBTextField().apply {
        var text = this.text
        addKeyPressListener(
            press = {
                text += it.keyChar
//                filePathsComboBox.apply {
//                    thread { findKeyboardInfo(text) }
//                }
            },
            release = {
                if (it.keyCode == KeyEvent.VK_BACK_SPACE) {
                    text = text.substring(0, text.length - 2)
//                    filePathsComboBox.apply {
//                        thread { findKeyboardInfo(text) }
//                    }
                }
            }
        )
    }.also { add(it) }
}

private fun ComboBox<String>.findKeyboardInfo(text: String) {
    // TODO change combobox state
}