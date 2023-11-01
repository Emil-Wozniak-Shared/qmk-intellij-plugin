package pl.ejdev.qmk.utils

import com.intellij.ide.plugins.PluginManager
import java.io.BufferedReader
import java.io.InputStreamReader

object IntellijIdeaResourceLoader {

    @Suppress("UnstableApiUsage")
    fun getResource(filePath: String): List<String> = PluginManager.getPlugins()
        .find { it.name == "Qmk" }
        ?.classLoader
        ?.getResourceAsStream(filePath)
        ?.let { BufferedReader(InputStreamReader(it)).lines().toList() }
        .orEmpty()
}
