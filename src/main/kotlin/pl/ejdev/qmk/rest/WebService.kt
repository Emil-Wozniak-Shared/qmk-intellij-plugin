package pl.ejdev.qmk.rest

import pl.ejdev.qmk.ui.models.keyboard.Keyboard
import pl.ejdev.qmk.ui.models.keyboard.KeyboardInfo
import pl.ejdev.qmk.ui.models.keyboard.Keyboards

internal object WebService {
    internal fun getKeyboards(): List<Keyboard> =
        request<Keyboards>(
            path = "keyboards",
            message = "fetch keyboards"
        ).keyboards

    internal fun getKeyboard(name: String): Keyboard =
        request<Keyboard>(
            path = "keyboards/$name",
            message = "fetch keyboard '$name'"
        )

    internal fun getKeyboardInfo(name: String): KeyboardInfo =
        request<KeyboardInfo>(
            path = "keyboards/$name/info",
            message = "fetch keyboard info '$name'"
        )
}

