package pl.ejdev.qmk.keyboard

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import pl.ejdev.qmk.ConfigFileReader
import pl.ejdev.qmk.model.*
import pl.ejdev.qmk.utils.list
import pl.ejdev.qmk.utils.text
import java.io.File

private const val QUOTED_SLASHES = "\"//\""
private const val HTTP = "http"
private const val NEW_LINE = "\n"
private const val KEYBOARD = "keyboard"
private const val LAYOUTS = "layouts"
private const val LAYOUT = "layout"
private const val LAYERS = "layers"

internal object KeyboardCapParser {
    private val parser = Parser.default()

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
        .filter { it.name.endsWith(".json") }
        .map(File::readLines)
        .mapNotNull { it.toJsonObject() }
        .filter { json -> json[KEYBOARD] != null && json[LAYOUT] != null && json[LAYERS] != null }
        .map { json ->
            val keyboard = json.text(KEYBOARD)
            val layout = json.text(LAYOUT)
            val layers = json.list<JsonArray<String>>(LAYERS).map(JsonArray<String>::value)
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
                    else -> line.replace("//.*".toRegex(), "")
                }
            }
            ?.filter(String::isNotEmpty)
            ?.joinToString(NEW_LINE)
            ?.let { parser.parse(it.byteInputStream()) as JsonObject? }

    @Suppress("UNCHECKED_CAST")
    private fun JsonObject.findLayout(name: String): List<KeyboardCap> {
        val layouts: JsonObject = get(LAYOUTS) as JsonObject
        val jsonObject: JsonObject = layouts[name] as JsonObject
        val layout: JsonArray<JsonObject> = jsonObject[LAYOUT] as JsonArray<JsonObject>
        return layout.value.map(KeyboardCap::from)
    }
}