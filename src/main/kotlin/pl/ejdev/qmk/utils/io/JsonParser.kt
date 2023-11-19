package pl.ejdev.qmk.utils.io

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser

private const val QUOTED_SLASHES = "\"//\""
private const val HTTP = "http"
private const val NEW_LINE = "\n"

private val parser = Parser.default()
private val klaxon = Klaxon()

fun  Any.toJson(): String= klaxon.toJsonString(this)

fun String.toJsonObject(): JsonObject? =
    this
        .split(NEW_LINE)
        .takeIf(List<String>::isNotEmpty)
        ?.let { lines ->
            lines.map(::sanitize)
                .filter(String::isNotEmpty)
                .joinToString(NEW_LINE)
                .byteInputStream()
                .let(parser::parse)
                .let { it as JsonObject }
        }

private val jsonComment = "//.*".toRegex()

private fun sanitize(line: String) = when {
    line.contains(HTTP) -> line
    line.contains(QUOTED_SLASHES) -> line
    else -> line.replace(jsonComment, "")
}
  