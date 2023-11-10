package pl.ejdev.qmk.window.components

import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextField

internal class PaneTabbed : JTabbedPane(RIGHT, WRAP_TAB_LAYOUT) {
    init {
        val p1 = JPanel().apply {
            add(JTextField(10))
        }
        val p2 = JPanel().apply {
            add(JLabel("sec"))
        }
        val p3 = JPanel().apply {
            add(JLabel("sec"))
            add(JTextField(10))
        }
        this.setBounds(40, 20, 800, 600)
        this.add("General", p1)
        this.add("Display", p2)
        this.add("About", p3)
    }
}