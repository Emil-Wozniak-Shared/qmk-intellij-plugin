package pl.ejdev.qmk.ui.service

import pl.ejdev.qmk.ui.models.layouts.Keyboard
import pl.ejdev.qmk.ui.models.layouts.LayoutSettings
import pl.ejdev.qmk.ui.utils.io.json.HomeDirQmkFilesReader
import pl.ejdev.qmk.ui.utils.io.json.LayoutParser
import pl.ejdev.qmk.ui.utils.io.json.LayoutSettingsParser
import pl.ejdev.qmk.ui.utils.nameWithout
import java.io.File

internal object KeyboardService {
    private const val LAYOUTS: String = "layouts"
    private lateinit var activeLayout: LayoutSettings.Layout
    private val qmkHomeDir: File = HomeDirQmkFilesReader.get()
    private val settingFile: File = HomeDirQmkFilesReader.getSettings()

    init {
        activeLayout = settings().layout
    }

    fun fromHomeDir(): List<Keyboard> =
        qmkHomeDir
            .listFiles()
            .orEmpty()
            .find { it.name == LAYOUTS }
            ?.listFiles()
            .orEmpty()
            .filter { it.name.endsWith(".json") }
            .map { jsonFile ->
                Keyboard(
                    filename = jsonFile.name,
                    meta = LayoutParser.parseMeta(jsonFile),
                    layouts = LayoutParser.parse(jsonFile, activeLayout.selected),
                    active = jsonFile.nameWithout() == activeLayout.file
                )
            }

    fun setActiveKeyboard(keyboard: Keyboard) {
        val settings = settings().copy(
            layout = LayoutSettings.Layout(
                file = keyboard.rawName,
                selected = keyboard.layouts.first().name
            )
        )
        HomeDirQmkFilesReader.setActiveLayoutFile(settings)
    }

    private fun settings(): LayoutSettings =
        settingFile.let(LayoutSettingsParser::parse)

}
