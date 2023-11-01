package pl.ejdev.qmk.utils

import com.intellij.ide.plugins.PluginManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

object IntellijIdeaResourceLoader {

    @Suppress("UnstableApiUsage")
    fun getResource(path: String): List<String> =
        PluginManager.getPlugins()
            .find { it.name == "Qmk" }
            ?.classLoader
            ?.getResourceAsStream(path)
            ?.let { BufferedReader(InputStreamReader(it)).lines().toList() }
            .orEmpty()

    fun listDir(name: String, depth: Int = 8): List<Path> = PluginManager.getPlugins()
        .find { it.name == "Qmk" }
        ?.pluginClassLoader!!
        .getResource(name)
        ?.toURI()
        ?.let { FileSystems.newFileSystem(it, mutableMapOf<String, Any>()) }
        ?.getPath(name)
        ?.let { Files.walk(it, depth) }
        ?.toList()
        .orEmpty()
}
