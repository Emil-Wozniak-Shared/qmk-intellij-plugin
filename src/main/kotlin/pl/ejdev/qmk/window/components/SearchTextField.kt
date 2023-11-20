package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.utils.listeners.addKeyPressListener
import java.awt.event.KeyEvent
import kotlin.concurrent.thread

fun Row.searchTextField(filePathsComboBox: Cell<ComboBox<String>>) {
    textField().apply {
        var text = component.text
        component.addKeyPressListener(
            press = {
                text += it.keyChar
                filePathsComboBox.apply {
                    thread { findKeyboardInfo(text) }
                }
            },
            release = {
                if (it.keyCode == KeyEvent.VK_BACK_SPACE) {
                    text = text.substring(0, text.length - 2)
                    filePathsComboBox.apply {
                        thread { findKeyboardInfo(text) }
                    }
                }
            }
        )
    }
}

private fun Cell<ComboBox<String>>.findKeyboardInfo(text: String) {
    // TODO change combobox state
}