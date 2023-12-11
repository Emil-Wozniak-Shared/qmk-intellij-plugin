package pl.ejdev.qmk.ui.components

import com.intellij.ui.components.JBBox
import pl.ejdev.qmk.ui.utils.joinByNewLine
import pl.ejdev.qmk.ui.basic.TIME_NEW_ROMAN_14
import java.io.File
import javax.swing.JOptionPane.showMessageDialog
import javax.swing.JPanel
import kotlin.concurrent.thread

private const val ELEMENT_WIDTH = 125
private const val ELEMENT_HEIGHT = 31
private const val Y_DIMENSION = 26
private const val START_POINT = 10

internal fun JBBox.uploadFilePanel(
    prefWidth: Int = ELEMENT_WIDTH * 4,
    prefHeight: Int = ELEMENT_HEIGHT * 3,
    action: (content: String) -> Unit
): JPanel = setupPanel(prefWidth, prefHeight) {
    var filename: String? = null

    fun checkSupportedType(it: String) {
        if (!it.endsWith("json")) {
            showMessageDialog(rootPane, "Please choose a valid file")
        }
    }

    val filenameTextField = textField {
        bounds {
            x = 125
            y = Y_DIMENSION
            width = ELEMENT_WIDTH
            height = ELEMENT_HEIGHT
        }
    }

    this.setLayout(null)
    this + filenameTextField
    this + jButton(name = "Import") {
        bounds {
            x = 258 + ELEMENT_WIDTH
            y = Y_DIMENSION
            width = 90
            height = ELEMENT_HEIGHT
        }
        addActionListener {
            thread {
                filename
                    .let { it ?: error("File not found") }
                    .also(::checkSupportedType)
                    .let(::File)
                    .runCatching(File::readLines)
                    .onFailure { showMessageDialog(rootPane, "Error while reading File : ${it.message}") }
                    .map(List<String>::joinByNewLine)
                    .map(action)
            }
        }
    }

    this + label("Keyboard setting") {
        isVisible = true
        setBounds(START_POINT, Y_DIMENSION, ELEMENT_WIDTH, ELEMENT_HEIGHT)
        setFont(TIME_NEW_ROMAN_14)
    }

    this + jButton(name = "Browse") {
        bounds {
            x = 258
            y = Y_DIMENSION
            width = ELEMENT_WIDTH
            height = ELEMENT_HEIGHT
        }
        addActionListener {
            fileChooser {
                val path = it.absolutePath
                filenameTextField.text = path
                filename = path
            }
        }
        font = TIME_NEW_ROMAN_14
    }
}.also(::add)


