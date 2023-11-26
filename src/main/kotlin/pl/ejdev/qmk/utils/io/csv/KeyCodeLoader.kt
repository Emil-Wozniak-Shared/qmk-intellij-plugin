package pl.ejdev.qmk.utils.io.csv

import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader

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
    companion object {
        fun from(chunks: List<String>): KeyCode {
            val (key, aliases, description) = chunks
            return KeyCode(
                key = key,
                aliases = aliases,
                description = description.formatNOOP()
                    .replace(" and ", " / ")
                    .replace("KC_".toRegex(), "")
            )
        }

        private fun String.formatNOOP() = if (!contains("(NOOP)")) this else "NOOP"
    }
}