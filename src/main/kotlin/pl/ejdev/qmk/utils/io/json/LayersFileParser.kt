package pl.ejdev.qmk.utils.io.json

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject

private const val NEW_LINE = "\n"
private const val LAYERS = "layers"
private const val QUOTED_SLASHES = "\"//\""
private const val HTTP = "http"
private val jsonComment = "//.*".toRegex()

internal object LayersFileParser {
    fun parse(configFileContent: String): List<List<String>> = configFileContent
        .split(NEW_LINE)
        .toJsonObject()
        .let(::requireNotNull)
        .list<JsonArray<String>>(LAYERS)
        .map(JsonArray<String>::value)

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
}