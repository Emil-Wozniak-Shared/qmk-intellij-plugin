package pl.ejdev.qmk.keyboard

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import pl.ejdev.qmk.model.Keyboard
import pl.ejdev.qmk.model.KeyboardCap

internal object KeyboardCapParser {
    private val parser = Parser.default()

    fun findKeyboard(lines: List<String>, layoutName: String): Keyboard? =
        fromResources(lines)
            ?.findLayout(name = layoutName)
            ?.let(::Keyboard)

    private fun fromResources(base: List<String>): JsonObject? = base
        .joinToString("\n")
        .let { parser.parse(it.byteInputStream()) as JsonObject? }

    @Suppress("UNCHECKED_CAST")
    private fun JsonObject.findLayout(name: String): List<KeyboardCap> {
        val layouts: JsonObject = get("layouts") as JsonObject
        val obj: JsonObject = layouts[name] as JsonObject
        val layout: JsonArray<JsonObject> = obj["layout"] as JsonArray<JsonObject>
        return layout.value.map(KeyboardCap::from)
    }

    fun cache() {

    }
}