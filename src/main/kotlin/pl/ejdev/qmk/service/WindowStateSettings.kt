package pl.ejdev.qmk.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jdom.Element
import pl.ejdev.qmk.ui.models.defaultLayout
import pl.ejdev.qmk.ui.models.layouts.Keyboard
import pl.ejdev.qmk.ui.service.KeyboardService

@Service
@State(
    name = WindowStateSettings.NAME,
    storages = [Storage("WindowStateService.xml")],
    defaultStateAsResource = true,
)
internal data class WindowStateSettings(
    var keyboard: String = "",
    var layers: List<List<String>> = defaultLayout,
    var keyboards: List<Keyboard> = emptyList(),
) : BasePersistStateElement() {
    internal companion object {
        const val NAME = "WindowStateService"
        const val KEYBOARD = "keyboard"
        const val LAYERS = "layers"
        const val KEYBOARDS = "keyboards"
    }

    override var currentState: Element = state

    init {
        KeyboardService.fromHomeDir().let { keyboards = it }
    }

    internal fun change(keyboard: String, setLayout: (Keyboard) -> Unit) {
        this.keyboard = keyboard
        this.keyboards = keyboards.map { kbd ->
            if (kbd.filename == keyboard) {
                setLayout(kbd)
                kbd.copy(
                    active = true,
                    layouts = kbd.layouts.mapIndexed { index, layout -> layout.copy(active = index == 0) }
                )
            } else kbd.copy(active = false, layouts = kbd.layouts.map { it.copy(active = false) })
        }
        state
    }

    override fun getState(): Element =
        Element(NAME).apply {
            this.setAttribute(KEYBOARD, keyboard)
            addContent(textCollection(LAYERS, layers))
            addContent(textCollection(KEYBOARDS, keyboards))
        }

    override fun loadState(element: Element) {
        keyboard = element.getAttributeValue(KEYBOARD)
        element.content().extract { (uri, value) ->
            when (uri) {
                KEYBOARDS -> keyboards = decode(value)
                LAYERS -> layers = decode(value)
            }
        }
    }
}