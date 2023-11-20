package pl.ejdev.qmk.utils.io.csv

import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader

private const val KEYCODES_PATH = "keycodes/keycodes.csv"
private const val SEPARATOR = ";"

object KeyCodeLoader {

    fun load(): List<KeyCode> = IntellijIdeaResourceLoader.getResource(KEYCODES_PATH)
        .getOrNull().orEmpty()
        .asSequence()
        .map { it.split(SEPARATOR) }
        .map(KeyCode.Companion::from)
        .toList()
}

data class KeyCode(
    val key: String,
    val aliases: String,
    val description: String,
) {
    companion object {
        fun from(chunks: List<String>): KeyCode = KeyCode(
            key = chunks[0],
            aliases = chunks[1],
            description = chunks[2]
        )
    }
}