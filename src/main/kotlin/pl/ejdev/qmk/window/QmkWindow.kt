@file:Suppress("UnstableApiUsage")

package pl.ejdev.qmk.window

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.DumbUtil
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.dsl.builder.panel
import pl.ejdev.qmk.service.KeyboardService
import pl.ejdev.qmk.state.FetcherKeyboardState
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCodeLoader
import pl.ejdev.qmk.window.components.UploadLayersBox
import pl.ejdev.qmk.window.tabbed.AppTabPanel
import pl.ejdev.qmk.window.tabbed.KeyBoardSwitch

internal class QmkWindow(private val toolWindow: ToolWindow) : DumbUtil, DumbAware {
    private var settingsManager = UISettings.getInstance()
    private val keyCodes = KeyCodeLoader.load()
    private val windowState: WindowState = WindowState()
    private val fetcherKeyboardState = FetcherKeyboardState()

    init {
        KeyboardService.fromHomeDir().let { windowState.keyboards = it }
    }

    private lateinit var keyBoardSwitch: KeyBoardSwitch

    val content = panel {
        row { AppTabPanel(fetcherKeyboardState).addToIJ(this) }
        row { UploadLayersBox(windowState, ::refreshKeyboard).addToIJ(this) }
        row { KeyBoardSwitch(windowState, settingsManager, keyCodes).also { keyBoardSwitch = it }.addToIJ(this) }
    }

    private fun refreshKeyboard() {
        keyBoardSwitch.refreshKeyboard()
    }

    @Suppress("UnstableApiUsage")
    override fun mayUseIndices(): Boolean = false
}




