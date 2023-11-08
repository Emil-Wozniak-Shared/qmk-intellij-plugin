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
            .asSequence()
            .filter { it.endsWith(infoFile) }
            .map(Path::pathString)
            .map { path ->
                path
                    .replace("keyboards/", "")
                    .replace("/$infoFile", "")
            }
            .toList()
            .let { keyboards ->
                keyboards.mapNotNull {
                    val lines = IntellijIdeaResourceLoader.getResource("keyboards/$it/$infoFile")
                    KeyboardLoader.extractKeyboardInfo(it, lines)
                }
            }
    }
}