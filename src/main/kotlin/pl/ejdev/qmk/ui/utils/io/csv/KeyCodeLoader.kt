package pl.ejdev.qmk.ui.utils.io.csv

import pl.ejdev.qmk.ui.utils.IntellijIdeaResourceLoader

internal object KeyCodeLoader {

    private const val KEYCODES_PATH = "keycodes/keycodes.csv"
    private const val SEPARATOR = ";"

    fun load(): List<KeyCode> =
        IntellijIdeaResourceLoader.getResource(KEYCODES_PATH)
            .getOrNull()
            .orEmpty()
            .drop(1)
            .asSequence()
            .map { it.split(SEPARATOR) }
            .map(KeyCode::from)
            .toList()
}
