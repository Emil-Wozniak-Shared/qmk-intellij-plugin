package pl.ejdev.qmk.state

import com.intellij.openapi.components.State
import pl.ejdev.qmk.models.keyboard.Keyboard
import pl.ejdev.qmk.models.keyboard.KeyboardInfo

@State(name = "fetcher state")
internal class FetcherKeyboardState {
    var keyboardName: String = ""
        set(value) {
            value.replace("[^A-Za-z0-9_\\-. ]".toRegex(), "")
            field = value
        }

    var keyboard: Keyboard? = null
    var keyboardInfo: KeyboardInfo? = null
}