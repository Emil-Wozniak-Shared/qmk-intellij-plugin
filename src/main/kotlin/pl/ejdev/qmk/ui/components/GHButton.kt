package pl.ejdev.qmk.ui.components

import com.intellij.icons.AllIcons
import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.ui.dsl.builder.Row
import javax.swing.JButton

fun Row.ghButton() {
    JButton("QMK repo", AllIcons.Webreferences.WebSocket).apply {
        addActionListener {
            BrowserLauncher.instance.open("https://github.com/qmk/qmk_firmware/tree/master/keyboards")
        }
    }.let { cell(it) }
}