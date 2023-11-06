package pl.ejdev.qmk.keycodes

private const val SEPARATOR = ";"

object KeyCodeParser {
    fun parse(resource: List<String>): List<KeyCode> = resource
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