package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import pl.ejdev.qmk.state.WindowState

internal fun Row.keyboardLayoutsComboBox(
    windowState: WindowState,
    refreshState: (keyboardName: String) -> Unit,
    refreshKeyboard: () -> Unit
): Cell<ComboBox<String>> = comboBox(windowState.layouts.map { it.filename })
    .onChange { selected ->
        refreshState(selected.toString())
        refreshKeyboard()
    }