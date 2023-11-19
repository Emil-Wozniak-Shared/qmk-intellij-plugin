package pl.ejdev.qmk.service

import pl.ejdev.qmk.KeyboardLoader
import pl.ejdev.qmk.model.KeyboardInfo
import pl.ejdev.qmk.utils.IntellijIdeaResourceLoader
import java.nio.file.Path
import kotlin.io.path.pathString

object SupportedKeyboardsService {
    fun loadAllSupportedKeyboards(): List<KeyboardInfo> {
        val infoFile = "info.json"
        return IntellijIdeaResourceLoader.listDir("keyboards")
            .map { paths ->
                paths
                    .asSequence()
                    .filter { it.endsWith(infoFile) }
                    .map(Path::pathString)
                    .map { path -> replaceSurrondingDetails(path, infoFile) }
                    .toList()
                    .let { keyboards ->
                        keyboards.mapNotNull { keyboard ->
                            val lines = IntellijIdeaResourceLoader.getResource("keyboards/$keyboard/$infoFile")
                                .getOrNull().orEmpty()
                            KeyboardLoader.extractKeyboardInfo(keyboard, lines)
                        }
                    }
                    .filter { info -> info.layouts.all { it != "null" } }
            }
            .getOrNull()
            .orEmpty()
    }

    private fun replaceSurrondingDetails(path: String, infoFile: String) = path
        .replace("keyboards/", "")
        .replace("/$infoFile", "")
}