package pl.ejdev.qmk.ui.utils.io.csv

import pl.ejdev.qmk.ui.basic.PluginIcons
import pl.ejdev.qmk.ui.utils.io.AsciiParser
import javax.swing.Icon

internal data class KeyCode(
    val key: String,
    val aliases: String,
    val description: String,
    val icon: Icon? = null
) {
    companion object {
        fun from(chunks: List<String>): KeyCode {
            val (key, aliases, description) = chunks
            return KeyCode(
                key = key,
                aliases = aliases,
                description = description.fixDescription(),
                icon = icon(key)
            )
        }

        private fun icon(key: String) =
            when {
                key.contains("RGB") -> PluginIcons.RGBIcon
                else -> null
            }

        private fun String.formatNOOP() = if (!contains("(NOOP)")) this else "NOOP"
        private fun String.fixDescription(): String =
            this.formatNOOP()
                .replace(" and ", "\t")
                .replace("KC_", "")
                .let(AsciiParser::parse)
    }

}