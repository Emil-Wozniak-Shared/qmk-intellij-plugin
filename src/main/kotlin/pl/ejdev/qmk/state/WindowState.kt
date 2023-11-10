package pl.ejdev.qmk.state

import pl.ejdev.qmk.model.KeyboardCap
import pl.ejdev.qmk.window.defaultLayout

data class WindowState(
    var caps: List<KeyboardCap> = emptyList(),
    var keyboard: String = "",
    var layout: String = "",
    var layers: List<List<String>> = defaultLayout,
    var lines: List<String> = emptyList()
)