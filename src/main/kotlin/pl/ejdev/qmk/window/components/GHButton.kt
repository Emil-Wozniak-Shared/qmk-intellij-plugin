package pl.ejdev.qmk.window.components

import com.intellij.icons.AllIcons
import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.ui.components.JBBox
import javax.swing.JButton

fun JBBox.ghButton() {
    JButton("QMK repo", AllIcons.Webreferences.WebSocket).apply {
        addActionListener {
            BrowserLauncher.instance.open("https://github.com/qmk/qmk_firmware/tree/master/keyboards")
        }
    }.let { add(it) }
}