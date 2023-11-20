package pl.ejdev.qmk.window.components

import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.json.LayersFileParser

internal fun Row.uploadLayersBox(windowState: WindowState, refreshKeyboard: () -> Unit) {
    fun importLayers(content: String): List<List<String>> =
        LayersFileParser.parse(content).also {
            windowState.layers = it
            refreshKeyboard()
        }
    cell(vbox { uploadFilePanel(action = ::importLayers) })
}