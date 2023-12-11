@file:Suppress("UnstableApiUsage")

package pl.ejdev.qmk

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.qmk.service.FetcherKeyboardSettings
import pl.ejdev.qmk.service.WindowStateSettings
import pl.ejdev.qmk.ui.components.UploadLayersBox
import pl.ejdev.qmk.ui.components.tabbed.AppTabPanel
import pl.ejdev.qmk.ui.components.tabbed.KeyBoardSwitch
import pl.ejdev.qmk.ui.utils.io.csv.KeyCode

internal class QmkWindow(
    private val toolWindow: ToolWindow,
    private val windowStateSettings: WindowStateSettings,
    private val fetcherKeyboardSettings: FetcherKeyboardSettings,
    private val keyCodes: List<KeyCode>
) : Window() {

    val content: DialogPanel = panel {
        row { AppTabPanel(fetcherKeyboardSettings).addToIJ(this) }
        row { UploadLayersBox(windowStateSettings, ::refreshKeyboard).addToIJ(this) }
        row { KeyBoardSwitch(windowStateSettings, settingsManager, keyCodes).also { keyBoardSwitch = it }.addToIJ(this) }
    }
}

internal abstract class Window : DumbUtil, DumbAware {
    protected var settingsManager = UISettings.getInstance()

    protected lateinit var keyBoardSwitch: KeyBoardSwitch

    protected fun refreshKeyboard() {
        keyBoardSwitch.refreshKeyboard()
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}




