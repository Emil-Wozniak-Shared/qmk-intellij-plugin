package pl.ejdev.qmk.window.components

import pl.ejdev.qmk.models.layouts.Keyboard
import pl.ejdev.qmk.models.layouts.Layout
import pl.ejdev.qmk.models.layouts.LayoutCell
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCode
import javax.swing.Box
import javax.swing.BoxLayout

internal class KeyCapsPanel(
    val layers: List<KeyCapsLayout> = emptyList()
) : Box(BoxLayout.Y_AXIS) {

    private fun build(
        layers: List<List<String>>,
        keyCodes: List<KeyCode>,
        windowState: WindowState,
        fontSize: Int
    ) {
        if (windowState.keyboards.isEmpty() || layers.isEmpty() || keyCodes.isEmpty()) {
            return
        }
        val activeLayout = windowState.keyboards.firstOrNull(Keyboard::active) ?: return
        val selectedLayout = activeLayout.layouts.firstOrNull(Layout::active) ?: return
        val capSize = fontSize * 6
        val width = selectedLayout.cells.maxOf(LayoutCell::w)
        val height = selectedLayout.cells.maxOf(LayoutCell::h)
        val groups = selectedLayout.cells.groupBy(LayoutCell::matrix)
        val codes = getKeyCodesForLayer(keyCodes, layers)
        layers.indices
            .asSequence()
            .map { layerIndex -> keyCapLayout(width, groups, height, codes, capSize, layerIndex) }
            .map { keyCapsLayout -> keyCapsLayout.addKeymaps(groups.values, capSize) }
            .forEach { this + it }
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

    constructor(
        layers: List<List<String>>,
        keyCodes: List<KeyCode>,
        windowState: WindowState,
        fontSize: Int
    ) : this() {
        build(layers, keyCodes, windowState, fontSize)
    }

    init {
        layers.forEach { this + it }
    }

    companion object {
        val Empty = KeyCapsPanel()
    }
}

