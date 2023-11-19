package pl.ejdev.qmk.state

import pl.ejdev.qmk.models.layouts.Layouts
import pl.ejdev.qmk.window.defaultLayout

internal data class WindowState(
    var keyboard: String = "",
    var layers: List<List<String>> = defaultLayout,
    var lines: List<String> = emptyList(),

    var layouts: List<Layouts> = emptyList()
)