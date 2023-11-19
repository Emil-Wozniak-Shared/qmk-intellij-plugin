package pl.ejdev.qmk.service

import pl.ejdev.qmk.models.layouts.LayoutSettings
import pl.ejdev.qmk.models.layouts.Layouts
import pl.ejdev.qmk.utils.io.HomeDirQmkFilesReader
import pl.ejdev.qmk.utils.io.LayoutParser
import pl.ejdev.qmk.utils.io.LayoutSettingsParser
import java.io.File

internal object LayoutService {
    private const val LAYOUTS: String = "layouts"
    private lateinit var activeLayout: LayoutSettings.Layout
    private val qmkHomeDir: File = HomeDirQmkFilesReader.get()
    private val settingFile: File = HomeDirQmkFilesReader.getSettings()

    init {
        activeLayout = settings().layout
    }

    fun fromHomeDir(): List<Layouts> =
        qmkHomeDir
            .listFiles()
            .orEmpty()
            .find { it.name == LAYOUTS }
            ?.listFiles()
            .orEmpty()
            .filter { it.name.endsWith(".json") }
            .map { jsonFile ->
                Layouts(
                    filename = jsonFile.name,
                    meta = LayoutParser.parseMeta(jsonFile),
                    layouts = LayoutParser.parse(jsonFile, activeLayout.selected),
                    active = jsonFile.name.replace(".json", "") == activeLayout.file
                )
            }

    fun setActiveLayout(layouts: Layouts) {
        val settings = settings().copy(layout = LayoutSettings.Layout(
            file = layouts.filename.replace(".json", ""),
            selected = layouts.layouts.first().name
        ))
        HomeDirQmkFilesReader.setActiveLayoutFile(settings)
    }

    private fun settings(): LayoutSettings =
        settingFile.let(LayoutSettingsParser::parse)

}
