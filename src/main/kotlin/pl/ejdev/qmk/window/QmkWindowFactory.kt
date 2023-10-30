package pl.ejdev.qmk.window

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

private const val LOCKED = false

class QmlWindowFactory : ToolWindowFactory, DumbAware {
    private val contentFactory: ContentFactory = ContentFactory.getInstance()
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow): Unit {
        QmkWindow(toolWindow)
            .let { contentFactory.createContent(it.content, null, LOCKED) }
            .let(toolWindow.contentManager::addContent)
    }
}