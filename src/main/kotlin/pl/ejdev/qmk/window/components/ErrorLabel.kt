package pl.ejdev.qmk.window.components

import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.window.ui.TIME_NEW_ROMAN_18

fun Row.errorLabel(error: String) {
    label(error).applyToComponent {
        font = TIME_NEW_ROMAN_18
        text = error
    }
}