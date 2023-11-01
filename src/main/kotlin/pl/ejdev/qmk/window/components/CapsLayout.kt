package pl.ejdev.qmk.window.components

import pl.ejdev.qmk.keycodes.KeyCode
import pl.ejdev.qmk.model.KeyboardCap
import java.awt.*
import javax.swing.JPanel
import kotlin.math.roundToInt

internal fun createKeyCaps(
    caps: List<KeyboardCap>,
    qmkLayers: List<List<String>>,
    keyCodes: List<KeyCode>,
): List<KeyCaps> {
    val groups = caps.groupBy { it.matrix[1] }
    val size = 60
    val width = caps.maxOf { it.w }
    val height = caps.maxOf { it.h }
    val keyCapLayers = qmkLayers.map { layer ->
        KeyCaps(
            width = (width * (size * size) * groups.maxOf { it.value.size }).roundToInt(),
            height = (height * (size * size) * groups.size).roundToInt(),
            labels = createLabels(layer, keyCodes)
        )
    }

    return keyCapLayers.subList(0, 1).mapIndexed { _, keyCaps ->
        groups.values.map { group ->
            addKeyCaps(group, size, keyCaps)
        }
        keyCaps
    }
}

private fun addKeyCaps(group: List<KeyboardCap>, size: Int, keyCaps: KeyCaps) = group.forEach { cap ->
    val x: Int = (cap.x * size).roundToInt()
    val y: Int = (cap.y * size).roundToInt()
    val w: Int = (cap.w * size).roundToInt()
    val h: Int = (cap.h * size).roundToInt()
    keyCaps.createKeyCap(x, y, w, h)
}

private fun createLabels(qmkLayout: List<String>, keyCodes: List<KeyCode>) = qmkLayout
    .map { keyCodes.find { kc -> kc.key == it }?.description ?: it }

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
        val graphics2D = graphics as Graphics2D
        keymaps
            .onEach { graphics.draw3DRect(it.x, it.y, it.width, it.height, true) }
            .onEachIndexed { index, it ->
                if (labels.isNotEmpty() && labels.size >= index) {
                    graphics2D.font = Font("DejaVu", Font.ROMAN_BASELINE, 10)
                    graphics2D.drawString(labels[index], it.x + 5, it.y + 25)
                }
            }
    }
}