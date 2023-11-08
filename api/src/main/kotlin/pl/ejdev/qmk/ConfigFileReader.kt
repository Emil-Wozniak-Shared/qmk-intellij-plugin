package pl.ejdev.qmk

import java.io.File
import java.nio.file.Paths
import kotlin.io.path.pathString

internal object ConfigFileReader {
    fun homeDirConfig(): Array<out File> {
        val homeDir = System.getProperty("user.home")
        val qmkDir = Paths.get(homeDir, ".qmk")
            .pathString
            .let(::File)
        if (!qmkDir.exists()) {
            qmkDir.mkdir()
        }
        return qmkDir.listFiles().orEmpty()
    }
}