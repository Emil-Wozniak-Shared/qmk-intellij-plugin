package pl.ejdev.qmk.ui.utils

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManager
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

internal object IntellijIdeaResourceLoader {
    private fun<T> T?.orRaise(message: String): T = this ?: error(message)

    fun getResource(path: String): Result<MutableList<String>> =
        PluginManager.getPlugins().runCatching {
            ideaPluginDescriptor()
                .classloader()
                .getResourceAsStream(path).orRaise("Resource not found: $path")
                .bufferedReader()
                .lines()
                .toList()
        }

    fun listDir(name: String, depth: Int = 8): Result<List<Path>> =
        PluginManager.getPlugins().runCatching {
            ideaPluginDescriptor()
                .classloader()
                .getResource(name).orRaise("Resource not found: $name")
                .toURI()
                .let { FileSystems.newFileSystem(it, mutableMapOf<String, Any>()) }
                .getPath(name)
                .let { Files.walk(it, depth) }
                .toList()
        }

    private fun Array<out IdeaPluginDescriptor>.ideaPluginDescriptor(): IdeaPluginDescriptor =
        find(IntellijIdeaResourceLoader::currentProject).orRaise("Plugin not found")

    @Suppress("UnstableApiUsage")
    private fun IdeaPluginDescriptor.classloader(): ClassLoader = this.classLoader.orRaise("No classloader found")

    private fun currentProject(descriptor: IdeaPluginDescriptor) = descriptor.name == "Qmk"
}
