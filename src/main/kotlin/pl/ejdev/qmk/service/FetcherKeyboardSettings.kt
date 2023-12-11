package pl.ejdev.qmk.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.diagnostic.Logger
import org.jdom.Element
import org.jdom.Text
import pl.ejdev.qmk.ui.models.keyboard.Keyboard
import pl.ejdev.qmk.ui.models.keyboard.KeyboardInfo
import pl.ejdev.qmk.ui.utils.io.json.toJson

@Service
@State(
    name = FetcherKeyboardSettings.NAME,
    storages = [Storage("fetcher_state.json")],
    defaultStateAsResource = true,
)
internal class FetcherKeyboardSettings(
    var keyboardName: String = "",
    var keyboard: Keyboard? = null,
    var keyboardInfo: KeyboardInfo? = null
) : BasePersistStateElement() {
    override var currentState: Element = this.getState()

   override fun getState(): Element = run {
        Element(NAME).apply {
            setAttribute(KEYBOARD_NAME, keyboardName)
            addContent(
                Element(KEYBOARD, KEYBOARD, KEYBOARD).apply {
                    addContent(Text(keyboard?.toJson()?: ""))
                }
            )
        }
    }

    override fun loadState(element: Element) {
        element.runCatching {
            keyboardName = getAttributeValue(KEYBOARD_NAME) ?: ""
            keyboard = getKeyboard()
        }.onFailure(LOG::error)
    }

    private fun Element.getKeyboard(): Keyboard {
        content().toList()
            .find { it.namespacesIntroduced.first().uri == KEYBOARD }
            ?.let {
                it.value
            }
//        return (get(KEYBOARD) as JsonObject).run {
//            Keyboard(
//                keyboardName = string(KEYBOARD_NAME),
//                maintainer = string(KEYBOARD_MAINTAINER),
//                manufacturer = string(KEYBOARD_MANUFACTURER),
//                path = string(KEYBOARD_PATH)
//            )
//        }
       return Keyboard()
    }

    internal companion object {
        const val NAME = "FetcherState"
        const val KEYBOARD_NAME = "keyboardName"
        const val KEYBOARD = "keyboard"
        const val KEYBOARD_MAINTAINER: String = ""
        const val KEYBOARD_MANUFACTURER: String = ""
        const val KEYBOARD_PATH: String = ""

        @JvmStatic
        val LOG: Logger = Logger.getInstance("qmkFetcher")
    }
}