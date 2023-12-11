package pl.ejdev.qmk.ui.components.tabbed

import com.intellij.BundleBase
import com.intellij.icons.AllIcons
import com.intellij.icons.AllIcons.Icons
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.components.*
import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.ui.models.keyboard.KeyboardInfo
import pl.ejdev.qmk.service.FetcherKeyboardSettings
import pl.ejdev.qmk.rest.WebClient
import pl.ejdev.qmk.ui.components.defaultKeyboardListener
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JComponent

internal class AppTabPanel(
    private val fetcherKeyboardSettings: FetcherKeyboardSettings,
) : JBTabbedPane() {

    init {
        val selectPanel = SelectPanel()
        val fetcherPanel = FetcherPanel(fetcherKeyboardSettings, selectPanel)
        listOf(
            Tab("Select", selectPanel, AllIcons.Diff.Arrow),
            Tab("Fetch", fetcherPanel, AllIcons.Actions.Search),
        ).forEachIndexed { index, (title, component, icon) ->
            insertTab(title, icon, component, "", index)
        }
    }

    fun addToIJ(row: Row) = row.cell(this)
}

internal data class Tab(
    val title: String,
    val component: JComponent,
    val icon: Icon = Icons.Ide.MenuArrow
)

internal class FetcherPanel(
    private val fetcherKeyboardSettings: FetcherKeyboardSettings,
    private val selectPanel: SelectPanel,
) : JBPanel<DialogPanel>(FlowLayout()) {
    private val webClient = WebClient

    init {
        JBLabel("Fetch keyboard settings: ").let { this.add(it) }
        JBTextField().apply {
            defaultKeyboardListener { fetcherKeyboardSettings.keyboardName = it }
        }.let { this.add(it) }
        button("Get config file") {
            if (fetcherKeyboardSettings.keyboardName.isNotEmpty()) {
                webClient.getKeyboard(fetcherKeyboardSettings.keyboardName)
                    .let { fetcherKeyboardSettings.keyboard = it }
            }
        }.let { this.add(it) }
        button("Get info file") {
            if (fetcherKeyboardSettings.keyboardName.isNotEmpty()) {
                webClient.getKeyboardInfo(fetcherKeyboardSettings.keyboardName)
                    .also { fetcherKeyboardSettings.keyboardInfo = it }
                    .also { selectPanel.refresh(it) }
            }
        }.let { this.add(it) }
        button("Get all possible") {
            webClient.getKeyboards()
        }.let { this.add(it) }
    }

    private fun button(text: String, actionListener: (event: ActionEvent) -> Unit) =
        JButton(BundleBase.replaceMnemonicAmpersand(text)).apply {
            addActionListener(actionListener)
        }
}

internal class SelectPanel : JBPanel<DialogPanel>(VerticalFlowLayout()) {
    private lateinit var header: JBBox
    private lateinit var columns: JBBox
    private lateinit var manufacturerLabel: JBLabel
    private lateinit var maintainerlabel: JBLabel
    private lateinit var layoutsLabel: JBLabel

    init {
        // header
        JBBox.createHorizontalBox().apply {
            this.isVisible = false
            add(JBBox.createHorizontalBox().apply {
                add(JBLabel("Manufacturer"))
            })
            add(JBBox.createHorizontalBox().apply {
                add(JBLabel("Maintainer"))
            })
            add(JBBox.createHorizontalBox().apply {
                add(JBLabel("Layouts"))
            })
        }.also {
            header = it
        } .let { this.add(it) }
        // columns
        JBBox.createHorizontalBox().apply {
            this.isVisible = false
            add(JBBox.createHorizontalBox().apply {
                add(JBLabel("").also { manufacturerLabel = it })
            })
            add(JBBox.createHorizontalBox().apply {
                add(JBLabel("").also { maintainerlabel = it })
            })
            add(JBBox.createHorizontalBox().apply {
                add(JBLabel("").also { layoutsLabel = it })
            })
        }.also { columns = it } .let { this.add(it) }
    }

    fun refresh(keyboardInfo: KeyboardInfo) {
        manufacturerLabel.text = keyboardInfo.manufacturer
        maintainerlabel.text = keyboardInfo.maintainer
        layoutsLabel.text = keyboardInfo.layouts.map { it.key }.joinToString(", ")
        manufacturerLabel.repaint()
        maintainerlabel.repaint()
        layoutsLabel.repaint()
        isVisible = true
        header.isVisible = true
        columns.isVisible = true
    }
}


