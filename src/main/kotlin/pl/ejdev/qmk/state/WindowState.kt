package pl.ejdev.qmk.state

import pl.ejdev.qmk.models.layouts.KeyboardLayouts
import pl.ejdev.qmk.window.defaultLayout

internal data class WindowState(
    var keyboard: String = "",
    var layers: List<List<String>> = defaultLayout,

    var layouts: List<KeyboardLayouts> = emptyList()
)