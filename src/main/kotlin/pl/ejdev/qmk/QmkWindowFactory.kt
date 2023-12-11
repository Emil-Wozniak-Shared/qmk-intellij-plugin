package pl.ejdev.qmk

import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import pl.ejdev.qmk.service.FetcherKeyboardSettings
import pl.ejdev.qmk.service.WindowStateSettings
import pl.ejdev.qmk.ui.utils.io.csv.KeyCode
import pl.ejdev.qmk.ui.utils.io.csv.KeyCodeLoader

class QmlWindowFactory : AbstractWindowFactory() {
    private val fetcherKeyboardSettings = service<FetcherKeyboardSettings>()
    private val windowStateSettings = service<WindowStateSettings>()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val qmkWindow = window(toolWindow)
        val content = contentFactory.createContent(qmkWindow.content, null, LOCKED)
        toolWindow.contentManager.apply {
            addContent(content)
            setSelectedContent(content)
        }
    }

    private fun window(toolWindow: ToolWindow) =
        QmkWindow(
            toolWindow = toolWindow,
            windowStateSettings = windowStateSettings,
            fetcherKeyboardSettings = fetcherKeyboardSettings,
            keyCodes = Settings.keyCodes
        )
}

abstract class AbstractWindowFactory : ToolWindowFactory, DumbAware {
    protected val contentFactory: ContentFactory = ContentFactory.getInstance()

    protected object Settings {
        internal val keyCodes: List<KeyCode> = KeyCodeLoader.load()
    }

    protected companion object {
        internal const val LOCKED = false
    }
}