package pl.ejdev.qmk.window.components

import com.intellij.ui.components.JBBox
import java.awt.Font
import java.awt.Font.PLAIN
import java.io.File
import javax.swing.JOptionPane.showMessageDialog
import kotlin.concurrent.thread

private val TIME_NEW_ROMAN_14 = Font("Times New Roman", PLAIN, 14)
private const val READ_FILE_MESSAGE = "File is reading...!!!"
private const val ELEMENT_WIDTH = 125
private const val ELEMENT_HEIGHT = 31
private const val Y_DIMENSION = 26
private const val START_POINT = 10

internal fun JBBox.uploadFileFrame(
    prefWidth: Int = ELEMENT_WIDTH * 4,
    prefHeight: Int = ELEMENT_HEIGHT * 3,
    action: (content: String) -> Unit
) = setupPanel(prefWidth, prefHeight) {
    var filename: String? = null

    fun checkSupportedType(it: String) {
        if (!it.endsWith("json")) {
            showMessageDialog(rootPane, "Please choose a valid file")
        }
    }

    val filenameTextField = textField {
        setBounds(125, Y_DIMENSION, ELEMENT_WIDTH, ELEMENT_HEIGHT)
    }

    this.setLayout(null)
    this + filenameTextField
    this + button(name = "Read") {
        setBounds(258 + ELEMENT_WIDTH, Y_DIMENSION, 90, ELEMENT_HEIGHT)
        addActionListener {
            thread {
                filename
                    .let { it ?: error("File not found") }
                    .also { checkSupportedType(it) }
                    .let { File(it) }
                    .runCatching(File::readLines)
                    .also { showMessageDialog(rootPane, READ_FILE_MESSAGE) }
                    .onFailure { showMessageDialog(rootPane, "Error while reading File : ${it.message}") }
                    .map { it.joinToString("\n") }
                    .map { action(it) }
            }
        }
    }
    this + label("Keyboard setting") {
        isVisible = true
        setBounds(START_POINT, Y_DIMENSION, ELEMENT_WIDTH, ELEMENT_HEIGHT)
        setFont(TIME_NEW_ROMAN_14)
    }
    this + button(name = "Browse") {
        setBounds(258, Y_DIMENSION, ELEMENT_WIDTH, ELEMENT_HEIGHT)
        addActionListener {
            fileChooser {
                val path = it.absolutePath
                filenameTextField.text = path
                filename = path
            }
        }
        setFont(TIME_NEW_ROMAN_14)
    }
}.also { add(it) }


