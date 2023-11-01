package pl.ejdev.qmk.keyboard

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import pl.ejdev.qmk.model.Keyboard
import pl.ejdev.qmk.model.KeyboardCap
import pl.ejdev.qmk.model.KeyboardInfo

private const val QUOTED_SLASHES = "\"//\""
private const val HTTP = "http"
private const val NEW_LINE = "\n"

internal object KeyboardCapParser {
    private val parser = Parser.default()

    fun findKeyboard(lines: List<String>, layoutName: String): Keyboard? =
        lines.toJsonObject()
            ?.findLayout(name = layoutName)
            ?.let(::Keyboard)

    fun extractKeyboardInfo(keyboard: String, lines: List<String>): KeyboardInfo? =
        lines.toJsonObject()?.let { json -> KeyboardInfo.from(keyboard, json) }

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
        val layouts: JsonObject = get("layouts") as JsonObject
        val jsonObject: JsonObject = layouts[name] as JsonObject
        val layout: JsonArray<JsonObject> = jsonObject["layout"] as JsonArray<JsonObject>
        return layout.value.map(KeyboardCap::from)
    }
}