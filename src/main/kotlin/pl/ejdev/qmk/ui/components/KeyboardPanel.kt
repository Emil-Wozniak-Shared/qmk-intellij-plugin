package pl.ejdev.qmk.ui.components

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBPanel
import org.jdesktop.swingx.HorizontalLayout
import pl.ejdev.qmk.service.WindowStateSettings
import pl.ejdev.qmk.ui.models.layouts.Keyboard
import pl.ejdev.qmk.ui.models.layouts.Layout
import pl.ejdev.qmk.ui.models.layouts.LayoutCell
import pl.ejdev.qmk.ui.utils.io.csv.KeyCode

internal class KeyboardPanel() : JBPanel<DialogPanel>(HorizontalLayout()) {
    private val layers: MutableList<KeyCapsLayout> = mutableListOf()

    private fun createLayers(
        layers: List<List<String>>,
        keyCodes: List<KeyCode>,
        windowStateSettings: WindowStateSettings,
        fontSize: Int
    ): List<KeyCapsLayout> {
        if (windowStateSettings.keyboards.isEmpty() || layers.isEmpty() || keyCodes.isEmpty()) {
            return emptyList()
        }
        val activeLayout = windowStateSettings.keyboards.firstOrNull(Keyboard::active) ?: return emptyList()
        val selectedLayout = activeLayout.layouts.firstOrNull(Layout::active) ?: return emptyList()
        val capSize = fontSize * 6
        val width = selectedLayout.cells.maxOf(LayoutCell::w)
        val height = selectedLayout.cells.maxOf(LayoutCell::h)
        val groups = selectedLayout.cells.groupBy(LayoutCell::matrix)
        val codes = getKeyCodesForLayer(keyCodes, layers)
        return layers.indices
            .asSequence()
            .map { layerIndex -> KeyCapsLayout(width, groups, height, codes, capSize, fontSize, layerIndex) }
            .map { keyCapsLayout -> keyCapsLayout.addKeymaps(groups.values, capSize) }
            .toList()
    }

    private fun getKeyCodesForLayer(keyCodes: List<KeyCode>, layers: List<List<String>>): List<List<KeyCode>> =
        layers.map { layer -> mapLayer(layer, keyCodes) }

    private fun mapLayer(layer: List<String>, keyCodes: List<KeyCode>) = layer.mapNotNull { cap ->
        keyCodes.find { keyCode ->
            val aliases = keyCode.aliases.split(",").map(String::trim)
            val specialKeyPrefix = cap.split("(")[0]
            aliases.any { alias -> alias == cap }
                    || keyCode.key == cap
                    || keyCode.key.startsWith(specialKeyPrefix)
                    || aliases.any { it.startsWith(specialKeyPrefix) }
        }
    }

    constructor(
        layers: List<List<String>>,
        keyCodes: List<KeyCode>,
        windowStateSettings: WindowStateSettings,
        fontSize: Int
    ) : this() {
        this.layers.addAll(createLayers(layers, keyCodes, windowStateSettings, fontSize))
        this.layers.forEach { this + it }
    }

    fun refresh(visibleLayer: Int) {
        this.layers.indices.forEach { index ->
            components[index].isVisible = index == visibleLayer
            components[index].repaint()
        }
        this.repaint()
    }
}

