package pl.ejdev.qmk.io

import pl.ejdev.qmk.specFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString

object KeyboardFilesReader {

    fun getkeyboards() {
        val path = "${System.getProperty("user.home")}/Pobrane/qmk/keyboards"
        val dir = File(path)

        listFilesUsingFileWalk(dir.path, Int.MAX_VALUE)
            .filter { it.endsWith(specFile) }
            .map {
                val target = "/home/ewozniak/dev/kt/untitled/src/main/resources/"
                it to it.replace("/home/ewozniak/Pobrane/qmk/", target)
            }
            .map { (old, new) ->
                val text = File(old).readLines().joinToString("\n")
                val file = File(new)
                val dirs = new.split("/")
                    .filter { !it.contains("json") }
                    .joinToString("/")
                println(dirs)
                Files.createDirectories(Paths.get(dirs))
                println(new)
                if (!file.exists()) {
                    file.createNewFile()
                    file.writeText(text)
                }
            }
    }

    @Throws(IOException::class)
    fun listFilesUsingFileWalk(dir: String, depth: Int): Set<String> {
        Files.walk(Paths.get(dir), depth).use { stream ->
            return stream
                .filter { file: Path ->
                    !Files.isDirectory(
                        file
                    )
                }
                .map { it.pathString }
                .map { it.toString() }
                .toList()
                .toSet()
        }
    }
}