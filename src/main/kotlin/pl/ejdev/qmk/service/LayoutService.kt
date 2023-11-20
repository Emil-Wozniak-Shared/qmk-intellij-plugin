package pl.ejdev.qmk.service

import pl.ejdev.qmk.models.layouts.KeyboardLayouts
import pl.ejdev.qmk.models.layouts.LayoutSettings
import pl.ejdev.qmk.utils.io.json.HomeDirQmkFilesReader
import pl.ejdev.qmk.utils.io.json.LayoutParser
import pl.ejdev.qmk.utils.io.json.LayoutSettingsParser
import pl.ejdev.qmk.utils.nameWithout
import java.io.File

internal object LayoutService {
    private const val LAYOUTS: String = "layouts"
    private lateinit var activeLayout: LayoutSettings.Layout
    private val qmkHomeDir: File = HomeDirQmkFilesReader.get()
    private val settingFile: File = HomeDirQmkFilesReader.getSettings()

    init {
        activeLayout = settings().layout
    }

    fun fromHomeDir(): List<KeyboardLayouts> =
        qmkHomeDir
            .listFiles()
            .orEmpty()
            .find { it.name == LAYOUTS }
            ?.listFiles()
            .orEmpty()
            .filter { it.name.endsWith(".json") }
            .map { jsonFile ->
                KeyboardLayouts(
                    filename = jsonFile.name,
                    meta = LayoutParser.parseMeta(jsonFile),
                    layouts = LayoutParser.parse(jsonFile, activeLayout.selected),
                    active = jsonFile.nameWithout() == activeLayout.file
                )
            }

    fun setActiveLayout(layouts: KeyboardLayouts) {
        val settings = settings().copy(
            layout = LayoutSettings.Layout(
                file = layouts.rawName,
                selected = layouts.layouts.first().name
            )
        )
        HomeDirQmkFilesReader.setActiveLayoutFile(settings)
    }

    private fun settings(): LayoutSettings =
        settingFile.let(LayoutSettingsParser::parse)

}
