package pl.ejdev.qmk.state

import pl.ejdev.qmk.model.KeyboardCap
import pl.ejdev.qmk.window.defaultLayout

data class WindowState(
    var caps: List<KeyboardCap> = emptyList(),
    var filePath: String = "ergodox_ez",
    var layoutName: String = "LAYOUT_ergodox",
    var layoutConfig: List<List<String>> = defaultLayout,
    var lines: List<String> = emptyList()
)
