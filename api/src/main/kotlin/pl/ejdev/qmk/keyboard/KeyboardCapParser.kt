package pl.ejdev.qmk.keyboard

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import pl.ejdev.qmk.ConfigFileReader
import pl.ejdev.qmk.model.*
import pl.ejdev.qmk.utils.list
import pl.ejdev.qmk.utils.`object`
import pl.ejdev.qmk.utils.text
import java.io.File

private const val QUOTED_SLASHES = "\"//\""
private const val HTTP = "http"
private const val NEW_LINE = "\n"
private const val KEYBOARD = "keyboard"
private const val LAYOUTS = "layouts"
private const val LAYOUT = "layout"
private const val LAYERS = "layers"
private const val JSON_FILE_EXT = ".json"

internal object KeyboardCapParser {
    private val parser = Parser.default()
    private val jsonComment = "//.*".toRegex()

    fun findKeyboard(lines: List<String>, layoutName: String): Keyboard? =
        lines.toJsonObject()
            ?.findLayout(name = layoutName)
            ?.let(::Keyboard)

    fun extractKeyboardInfo(keyboard: String, lines: List<String>): KeyboardInfo? =
        lines.toJsonObject()?.let { json -> KeyboardInfo.from(keyboard, json) }

    fun parseConfigFile(configFileContent: String): KeyboardLayers = configFileContent
        .split(NEW_LINE)
        .toJsonObject()
        .let(::requireNotNull)
        .list<JsonArray<String>>(LAYERS)
        .map(JsonArray<String>::value)

    fun systemFiles(): SystemConfigSettings? = ConfigFileReader.homeDirConfig()
        .asSequence()
        .filter { it.name.endsWith(JSON_FILE_EXT) }
        .map(File::readLines)
        .mapNotNull { it.toJsonObject() }
        .filter { json -> json["layouts"] != null }
        .map { json ->
            val keyboard = json.text(KEYBOARD)
            val layout = json.text("layouts")
            val layers = json.list<JsonArray<String>>(LAYERS)
                .map(JsonArray<String>::value)
            SystemConfigSettings(keyboard, layout, layers)
        }
        .firstOrNull()

    private fun List<String>.toJsonObject(): JsonObject? =
        this
            .takeIf(List<String>::isNotEmpty)
            ?.map { line ->
                when {
                    line.contains(HTTP) -> line
                    line.contains(QUOTED_SLASHES) -> line
                    else -> line.replace(jsonComment, "")
                }
            }
            ?.filter(String::isNotEmpty)
            ?.joinToString(NEW_LINE)
            ?.let { parser.parse(it.byteInputStream()) as JsonObject? }

    private fun JsonObject.findLayout(name: String): List<KeyboardCap> {
        val layouts: JsonObject = this.`object`(LAYOUTS)
        val jsonObject: JsonObject = layouts.`object`(name)
        val layout: List<JsonObject> = jsonObject.list(LAYOUT)
        return layout.map(KeyboardCap::from)
    }
}