package pl.ejdev.qmk.ui.utils.io

internal object AsciiParser {
    fun parse(text: String): String = when (text) {
        // Delete
        "Delete (Backspace)" -> "⌫"
        "Forward Delete" -> "⌦"
        // Action keys
        "Escape" -> "⎋"
        "Return (Enter)" -> "⏎"
        "Left Shift" -> "⇧ ⬅"
        "Right Shift" -> "⇧ ⮕"
        "Left Control" -> "^ ←"
        "Right Control" -> "^ →"
        "Left Alt (Option)" -> "⎇ ←"
        "Right Alt (Option/AltGr)" -> "⎇ →"
        "Use the next lowest non-transparent key" -> "▒"
        "Application (Windows Context Menu Key)" -> "☰"
        "Home" -> HOME
        "Page Up" -> PAGE_UP
        "Page Down" -> PAGE_DOWN
        "Insert" -> INSERT
        "Print Screen" -> "⎙"
        "End" -> END
        // Numpad
        "Keypad Num Lock and Clear" -> "⇭"
        "Keypad 1 and End" -> "➊  / $END"
        "Keypad 2 and Down Arrow" -> "➋  / ↓"
        "Keypad 3 and Page Down" -> "➌  / $PAGE_DOWN"
        "Keypad 4 and Left Arrow" -> "➍  / ←"
        "Keypad 5" -> " ➎ "
        "Keypad 6 and Right Arrow" -> "➏  / →"
        "Keypad 7 and Home" -> "➐  / $HOME"
        "Keypad 8 and Up Arrow" -> "➑  / ↑"
        "Keypad 9 and Page Up" -> "➒  / $PAGE_UP"
        "Keypad 0 and Insert" -> " \uD83C\uDD0C / $INSERT"
        "Mail" -> "✉"
        //
        "Right GUI (Windows/Command/Meta key)" -> "Right $META"
        "Left GUI (Windows/Command/Meta key)" -> "Left $META"
        "Disable the GUI keys" -> "$META Off"
        "Enable the GUI keys" -> "$META On"
        "Escape when pressed, <code>`</code> when Shift or GUI are held" -> "⇧ / $META + `"
        // Arrow
        "Up Arrow" -> "⬆"
        "Down Arrow" -> "⬇"
        "Left Arrow" -> "⬅"
        "Right Arrow" -> "⮕"
        // Mouse
        "Mouse Cursor Up" -> "$MOUSE Up"
        "Mouse Cursor Down" -> "$MOUSE Down"
        "Mouse Cursor Left" -> "$MOUSE Left"
        "Mouse Cursor Right" -> "$MOUSE Right"
        "Mouse Button 1" -> "$MOUSE 1"
        "Mouse Button 2" -> "$MOUSE 2"
        "Mouse Button 3" -> "$MOUSE 3"
        "Mouse Button 4" -> "$MOUSE 4"
        "Mouse Button 5" -> "$MOUSE 5"
        // Browser
        "Browser Search" -> "$BROWSER $SEARCH"
        "Browser Forward" -> "$BROWSER ⏩"
        "Browser Back" -> "$BROWSER ⏪"
        // Media
        "Volume Up" -> VOLUME_UP
        "Volume Down" -> VOLUME_DOWN
        "Play/Pause Track" -> "⏯"
        "Stop Track" -> "⏹"
        "Next Track" -> "⏭"
        "Previous Track" -> "⏮"
        "Mute" -> VOLUME_MUTE
        // Characters
        "Caps Lock" -> CAPSLOCK
        "Spacebar" -> "␣"
        // Brightness
        "Brightness Up" -> BRIGHTNESS_UP
        "Brightness Down" -> BRIGHTNESS_DOWN
        // Devices
        "Eject" -> "⏏"
        // RGB
        "Toggle RGB lighting on or off" -> "On / Off"
        "Cycle through modes, reverse direction when Shift is held" -> FORWARD
        "Cycle through modes in reverse, forward direction when Shift is held" -> BACKWARD
        "Static (no animation) mode" -> "Static"
        "Increase value (brightness), decrease value when Shift is held" -> BRIGHTNESS_UP
        "Decrease value (brightness), increase value when Shift is held" -> BRIGHTNESS_DOWN
        // Others
        "Reinitializes the keyboard's EEPROM (persistent memory)" -> "E²PROM"
        else -> text
    }

    private const val MOUSE = "\uD83D\uDDB1"
    private const val VOLUME_UP = "\uD83D\uDD0A"
    private const val VOLUME_DOWN = "\uD83D\uDD09"
    private const val VOLUME_MUTE = "\uD83D\uDD07"
    private const val BROWSER = "\uD83C\uDF10"
    private const val SEARCH = "\uD83D\uDD0D"
    private const val CAPSLOCK = "\uD83C\uDD30"
    private const val META = "◆"
    private const val BRIGHTNESS_UP = "☀+"
    private const val BRIGHTNESS_DOWN = "☀-"
    private const val HOME = "⌂"
    private const val PAGE_UP = "⇞"
    private const val PAGE_DOWN = "⇟"
    private const val INSERT = "⎀"
    private const val END = "⤓"
    private const val FORWARD = "⏩"
    private const val BACKWARD = "⏪"

}