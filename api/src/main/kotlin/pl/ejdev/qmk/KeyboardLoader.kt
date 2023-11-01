package pl.ejdev.qmk

import pl.ejdev.qmk.keyboard.KeyboardCapParser
import pl.ejdev.qmk.model.Keyboard
import pl.ejdev.qmk.model.KeyboardInfo

object KeyboardLoader {
    fun load(base: List<String>, layoutName: String): Keyboard? = KeyboardCapParser.findKeyboard(base, layoutName)

    fun extractKeyboardInfo(keyboard: String, lines: List<String>): KeyboardInfo? =
        KeyboardCapParser.extractKeyboardInfo(keyboard, lines)

}

