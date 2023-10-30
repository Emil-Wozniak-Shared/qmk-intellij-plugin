package pl.ejdev.qmk

import pl.ejdev.qmk.keyboard.KeyboardCapParser
import pl.ejdev.qmk.model.Keyboard

object KeyboardLoader {
    fun load(base: List<String>, layoutName: String): Keyboard? = KeyboardCapParser.findKeyboard(base, layoutName)

    fun cacheKeyboards() = KeyboardCapParser.cache()
}

