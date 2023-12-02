package pl.ejdev.qmk.window.components

import pl.ejdev.qmk.models.layouts.Keyboard
import pl.ejdev.qmk.models.layouts.Layout
import pl.ejdev.qmk.models.layouts.LayoutCell
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCode
import javax.swing.Box
import javax.swing.BoxLayout

internal fun keyCapsPanel(
    layers: List<List<String>>,
    keyCodes: List<KeyCode>,
    windowState: WindowState
): KeyCapsPanel {
    if (windowState.keyboards.isEmpty() || layers.isEmpty() || keyCodes.isEmpty()) {
        return KeyCapsPanel.Empty
    }
    val activeLayout = windowState.keyboards.firstOrNull(Keyboard::active) ?: return KeyCapsPanel.Empty
    val selectedLayout = activeLayout.layouts.firstOrNull(Layout::active) ?: return KeyCapsPanel.Empty
    val capSize = 75
    val width = selectedLayout.cells.maxOf(LayoutCell::w)
    val height = selectedLayout.cells.maxOf(LayoutCell::h)
    val groups = selectedLayout.cells.groupBy(LayoutCell::matrix)
    val codes = getKeyCodesForLayer(keyCodes, layers)
    val keyCapLayers = layers.indices
        .map { layerIndex -> keyCapLayout(width, groups, height, codes, capSize, layerIndex) }
        .map { keyCapsLayout -> keyCapsLayout.addKeymaps(groups.values, capSize) }

    return KeyCapsPanel(keyCapLayers)
}


private fun getKeyCodesForLayer(keyCodes: List<KeyCode>, layers: List<List<String>>): List<List<KeyCode>> =
    layers.map { layer ->
        layer.mapNotNull {
            keyCodes.find { keyCode ->
                val aliases = keyCode.aliases.split(",").map(String::trim)
                val specialKeyPrefix = it.split("(")[0]
                aliases.any { alias -> alias == it }
                        || keyCode.key == it
                        || keyCode.key.startsWith(specialKeyPrefix)
                        || aliases.any { it.startsWith(specialKeyPrefix) }
            }
        }
    }

internal class KeyCapsPanel(
    val layers: List<KeyCapsLayout> = emptyList()
) : Box(BoxLayout.Y_AXIS) {
    init {
        layers.forEach { this + it }
    }

    companion object {
        val Empty = KeyCapsPanel()
    }
}

