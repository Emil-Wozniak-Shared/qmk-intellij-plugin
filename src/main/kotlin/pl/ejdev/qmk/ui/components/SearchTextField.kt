package pl.ejdev.qmk.ui.components

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JTextField

fun JTextField.defaultKeyboardListener(
    release: (String) -> Unit
) {
    this.addKeyListener(object : KeyAdapter() {
        override fun keyReleased(e: KeyEvent) {
            release(text)
        }

        override fun keyTyped(e: KeyEvent) {
        }

        override fun keyPressed(e: KeyEvent) {
        }
    })
}
