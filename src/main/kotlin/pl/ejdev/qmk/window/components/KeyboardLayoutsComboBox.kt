package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBBox
import pl.ejdev.qmk.state.WindowState

internal fun JBBox.keyboardLayoutsComboBox(
    windowState: WindowState,
    refreshState: (keyboardName: String) -> Unit,
    refreshKeyboard: () -> Unit
): ComboBox<String> =
    ComboBox((windowState.keyboards.map { it.filename }).toTypedArray())
        .onChange { selected ->
            refreshState(selected.toString())
            refreshKeyboard()
        }
        .also { add(it) }