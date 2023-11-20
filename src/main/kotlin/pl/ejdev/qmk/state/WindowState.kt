package pl.ejdev.qmk.state

import pl.ejdev.qmk.models.layouts.Keyboard
import pl.ejdev.qmk.window.defaultLayout

internal data class WindowState(
    var keyboard: String = "",
    var layers: List<List<String>> = defaultLayout,
    var keyboards: List<Keyboard> = emptyList()
) {
    fun change(keyboard: String, setLayout: (Keyboard) -> Unit) {
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
    }
}