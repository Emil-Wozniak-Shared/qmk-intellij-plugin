package pl.ejdev.qmk.utils.listeners

import com.intellij.ui.components.JBTextField
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

fun JBTextField. addKeyPressListener(
    press: (KeyEvent) -> Unit,
    release: (KeyEvent) -> Unit,
) {
    val listener = object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            super.keyPressed(e)
            press(e)
        }

        override fun keyReleased(e: KeyEvent) {
            super.keyReleased(e)
            release(e)
        }
    }
    addKeyListener(listener)
}