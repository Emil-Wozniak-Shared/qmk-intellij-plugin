package pl.ejdev.qmk.window.components

import com.intellij.ui.JBColor
import pl.ejdev.qmk.keycodes.KeyCode
import pl.ejdev.qmk.models.layouts.LayoutCell
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.window.ui.KEYCAP_DARK
import pl.ejdev.qmk.window.ui.KEYCAP_LIGHT
import java.awt.*
import javax.swing.JPanel
import kotlin.math.roundToInt

internal fun createKeyCaps(
    layers: List<List<String>>,
    keyCodes: List<KeyCode>,
    windowState: WindowState
): List<JPanel> {
    if (windowState.layouts.isEmpty() || layers.isEmpty() || keyCodes.isEmpty()) {
        return listOf(JPanel())
    }
    val activeLayout = windowState.layouts.firstOrNull { it.active } ?: return listOf(JPanel())
    val selectedLayout = activeLayout.layouts.firstOrNull { it.active } ?: return listOf(JPanel())
    val size = 60
    val width = selectedLayout.cells.maxOf { it.w }
    val height = selectedLayout.cells.maxOf { it.h }
    val groups =  selectedLayout.cells.groupBy { it.matrix }
    val keyCapLayers = layers.map { layer ->
        KeyCaps(
            width = (width * (size * size) * groups.maxOf { it.value.size }).roundToInt(),
            height = (height * (size * size) * groups.size).roundToInt(),
            labels = createLabels(layer, keyCodes)
        )
    }

    return keyCapLayers.subList(0, 1).mapIndexed { _, keyCaps ->
        keyCaps.apply {
            groups.values.map { group ->
                addKeyCaps(group, size)
            }
        }
    }
}

private fun KeyCaps.addKeyCaps(cellGroups: List<LayoutCell>, size: Int) =
    cellGroups.forEach { cell ->
        val x: Int = (cell.x * size).roundToInt()
        val y: Int = (cell.y * size).roundToInt()
        val w: Int = (cell.w * size).roundToInt()
        val h: Int = (cell.h * size).roundToInt()
        this.createKeyCap(x, y, w, h)
    }

private fun createLabels(qmkLayout: List<String>, keyCodes: List<KeyCode>): List<String> =
    qmkLayout.map { layout -> keyCodes.find { keyCode -> keyCode.key == layout }?.description ?: layout }

internal class KeyCaps(
    private val width: Int = 100,
    private val height: Int = 100,
    private val labels: List<String>
) : JPanel() {

    private val keymaps: MutableList<Rectangle> = mutableListOf()
    fun createKeyCap(x: Int, y: Int, width: Int, height: Int) {
        val keycap = Rectangle(x, y, width, height)
        keymaps.add(keycap)
    }

    override fun getPreferredSize(): Dimension = Dimension(width, height)


    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        keymaps
            .onEach {
                graphics.in2D {
                    drawBox(it, JBColor.WHITE, 0)
                    drawBox(it, background = KEYCAP_DARK, padding = 1)
                    drawBox(it, background = KEYCAP_LIGHT, padding = 4)
                }
            }
            .onEachIndexed { index, rectangle ->
                if (labels.isNotEmpty() && labels.size >= index) {
                    graphics.in2D {
                        color = JBColor.WHITE
                        font = Font("DejaVu", Font.ROMAN_BASELINE, 10)
                        drawString(labels[index], rectangle.x + 5, rectangle.y + 25)
                    }
                }
            }
    }

    private fun Graphics2D.drawBox(rectangle: Rectangle, background: Color, padding: Int) {
        color = background
        fillRect(
            rectangle.x + padding,
            rectangle.y + padding,
            rectangle.width - padding * 2,
            rectangle.height - padding * 2
        )
        drawRoundRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, 10, 10)
    }

    private fun Graphics.in2D(dsl: Graphics2D.() -> Unit) = dsl(this as Graphics2D)
}