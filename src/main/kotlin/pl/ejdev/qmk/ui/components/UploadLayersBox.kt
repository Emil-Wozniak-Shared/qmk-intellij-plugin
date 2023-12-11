package pl.ejdev.qmk.ui.components

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBBox
import com.intellij.ui.components.JBPanel
import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.service.WindowStateSettings
import pl.ejdev.qmk.ui.utils.io.json.LayersFileParser
import java.awt.FlowLayout

internal class UploadLayersBox(
    private val windowStateSettings: WindowStateSettings,
    private val refreshKeyboard: () -> Unit
) : JBPanel<DialogPanel>(FlowLayout()) {
    private fun importLayers(content: String): List<List<String>> =
        LayersFileParser.parse(content).also {
            windowStateSettings.layers = it
            refreshKeyboard()
        }

    init {
        add(JBBox.createVerticalBox().apply {
            uploadFilePanel(action = ::importLayers)
        })
    }

    fun addToIJ(row: Row) = row.cell(this)
}