package pl.ejdev.qmk.ui.utils.io.json

import pl.ejdev.qmk.ui.models.layouts.LayoutSettings
import java.io.File

internal object LayoutSettingsParser {
    private const val NEW_LINE = "\n"
    private const val DEFAULT_LAYOUT = "layout"

    fun parse(file: File): LayoutSettings =
        file
            .readLines()
            .joinToString(NEW_LINE)
            .toJsonObject()
            ?.let { jsonObject ->
                val defaultLayout = jsonObject.`object`(DEFAULT_LAYOUT)
                val file = defaultLayout.text("file")
                val selected = defaultLayout.text("selected")
                LayoutSettings.Layout(file, selected)
                LayoutSettings(layout = LayoutSettings.Layout(file, selected))
            }
            ?: LayoutSettings()
}
