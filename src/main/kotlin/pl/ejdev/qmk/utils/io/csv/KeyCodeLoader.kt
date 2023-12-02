package pl.ejdev.qmk.utils.io.csv

import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import pl.ejdev.qmk.window.ui.PluginIcons
import javax.swing.Icon

private const val KEYCODES_PATH = "keycodes/keycodes.csv"
private const val SEPARATOR = ";"

object KeyCodeLoader {

    fun load(): List<KeyCode> =
        IntellijIdeaResourceLoader.getResource(KEYCODES_PATH)
            .getOrNull().orEmpty()
            .asSequence()
            .drop(1)
            .map { it.split(SEPARATOR) }
            .map(KeyCode::from)
            .toList()
}

data class KeyCode(
    val key: String,
    val aliases: String,
    val description: String,
) {
    fun isNOOP() = key.contains("NOOP")

    companion object {
        fun from(chunks: List<String>): KeyCode {
            val (key, aliases, description) = chunks
            return KeyCode(
                key = key,
                aliases = aliases,
                description = description
                    .formatNOOP()
                    .replace(" and ", " / ")
                    .replace("KC_", "")
            )
        }


        private fun String.formatNOOP() = if (!contains("(NOOP)")) this else "NOOP"
    }
}

fun KeyCode.toGraphic(
    onSpecialKey: (Icon) -> Unit,
    onDefault: (String) -> Unit
) = when (key) {
    "RGB_TOG" -> onSpecialKey(PluginIcons.RGBIcon)
    else -> onDefault(description)
}
