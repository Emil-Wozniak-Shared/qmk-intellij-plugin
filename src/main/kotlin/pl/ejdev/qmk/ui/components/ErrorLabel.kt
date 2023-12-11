package pl.ejdev.qmk.ui.components

import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.ui.basic.TIME_NEW_ROMAN_18

fun Row.errorLabel(error: String) {
    label(error).applyToComponent {
        font = TIME_NEW_ROMAN_18
        text = error
    }
}