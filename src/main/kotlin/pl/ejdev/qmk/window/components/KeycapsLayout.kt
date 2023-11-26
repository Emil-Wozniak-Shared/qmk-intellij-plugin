package pl.ejdev.qmk.window.components

import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import pl.ejdev.qmk.models.layouts.LayoutCell
import pl.ejdev.qmk.state.WindowState
import pl.ejdev.qmk.utils.io.csv.KeyCode
import pl.ejdev.qmk.window.ui.DEJAVU_14_BOLD
import pl.ejdev.qmk.window.ui.KEYCAP_DARK
import pl.ejdev.qmk.window.ui.KEYCAP_LIGHT
import pl.ejdev.qmk.window.ui.PluginIcons
import java.awt.*
import java.awt.font.FontRenderContext
import java.lang.Math.round
import javax.swing.*
import kotlin.math.roundToInt


internal fun keyCapsPanel(
    layers: List<List<String>>,
    keyCodes: List<KeyCode>,
    windowState: WindowState
): KeyCapsPanel {
    if (windowState.keyboards.isEmpty() || layers.isEmpty() || keyCodes.isEmpty()) {
        return KeyCapsPanel.Empty
    }
    val activeLayout = windowState.keyboards.firstOrNull { it.active } ?: return KeyCapsPanel.Empty
    val selectedLayout = activeLayout.layouts.firstOrNull { it.active } ?: return KeyCapsPanel.Empty
    val size = 90
    val width = selectedLayout.cells.maxOf { it.w }
    val height = selectedLayout.cells.maxOf { it.h }
    val groups = selectedLayout.cells.groupBy { it.matrix }
    val keyCapLayers = layers.mapIndexed { index, layer ->
        KeyCaps(
            width = (width * (size * size) * groups.maxOf { it.value.size }).roundToInt(),
            height = (height * (size * size) * groups.size).roundToInt(),
            labels = createLabels(layer, keyCodes),
            visible = index == 0
        )
    }

    val caps: List<KeyCaps> = keyCapLayers
        .mapIndexed { _, keyCaps ->
            keyCaps.apply {
                groups.values.map { group ->
                    addKeyCaps(group, size)
                }
            }
        }

    return KeyCapsPanel(caps)
}

internal class KeyCapsPanel(
    val layers: List<KeyCaps> = listOf()
) : Box(BoxLayout.Y_AXIS) {
    init {
        layers.forEach {
            this + it
        }
    }

    companion object {
        val Empty = KeyCapsPanel()
    }
}

private fun KeyCaps.addKeyCaps(cellGroups: List<LayoutCell>, size: Int) =
    cellGroups.forEach { cell ->
        val shape = rectangle(cell, size)
        this.addToKeymap(shape)
    }

private fun rectangle(cell: LayoutCell, size: Int): Rectangle {
    val x: Int = (cell.x * size).roundToInt()
    val y: Int = (cell.y * size).roundToInt()
    val w: Int = (cell.w * size).roundToInt()
    val h: Int = (cell.h * size).roundToInt()
    return Rectangle(x, y, w, h)
}

private fun createLabels(qmkLayout: List<String>, keyCodes: List<KeyCode>): List<String> =
    qmkLayout.map { layout ->
        keyCodes
            .find { keyCode -> keyCode.key == layout }
            ?.description
            ?: layout
    }

internal class KeyCaps(
    private val width: Int = 100,
    private val height: Int = 100,
    private val labels: List<String>,
    private var visible: Boolean = true
) : JPanel() {
    init {
        isVisible = visible
    }

    private val keymap: MutableList<Rectangle> = mutableListOf()
    internal fun addToKeymap(shape: Rectangle) = keymap.add(shape)

    override fun getPreferredSize(): Dimension = Dimension(width, height)

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        keymap
            .onEachIndexed { index, rectangle ->
                val isLabels = labels.isNotEmpty() && labels.size >= index
                val isNOOP = isLabels && labels[index].contains("NOOP")
                graphics.in2D {
                    drawBox(rectangle, JBColor.WHITE, 0)
                    drawBox(rectangle, background = if (isNOOP) JBColor.DARK_GRAY else KEYCAP_DARK, padding = 1)
                    drawBox(rectangle, background = if (isNOOP) JBColor.LIGHT_GRAY else KEYCAP_LIGHT, padding = 4)
                }
                if (isLabels) {
                    graphics.in2D {
                        color = Gray._255
                        val label = labels[index]
                        if (label == "Toggle RGB lighting on or off") {
                            graphics.drawImage(
                                PluginIcons.RGBIcon.iconToImage(),
                                rectangle.x + 8,
                                rectangle.y + 8,
                                rectangle.width - 8 - 8,
                                rectangle.height - 8 - 8,
                                this@KeyCaps
                            )
                        } else {
                            centerString(rectangle = rectangle, text = label, font = DEJAVU_14_BOLD)
                        }
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

    /**
     * This method centers a `String` in
     * a bounding `Rectangle`.
     * @param rectangle - The bounding `Rectangle`.
     * @param text - The `String` to center in the
     * bounding rectangle.
     * @param font - The display font of the `String`
     */
    private fun Graphics.centerString(
        rectangle: Rectangle,
        text: String,
        font: Font
    ) {
        val frc = FontRenderContext(null, true, true)
        val r2D = font.getStringBounds(text, frc)
        val rWidth = round(r2D.width).toInt()
        val rHeight = round(r2D.height).toInt()
        val rX = round(r2D.x).toInt()
        val rY = round(r2D.y).toInt()
        val a = rectangle.width / 2 - rWidth / 2 - rX
        val b = rectangle.height / 2 - rHeight / 2 - rY
        this.font = font
        this.drawString(text, rectangle.x + a, rectangle.y + b)
    }

    private fun Icon.iconToImage(x: Int = 0, y: Int = 0): Image =
        if (this is ImageIcon) image
        else {
            val image = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .defaultScreenDevice
                .defaultConfiguration.createCompatibleImage(iconWidth, iconHeight)
            val graphics2D = image.createGraphics()
            paintIcon(null, graphics2D, x, y)
            graphics2D.dispose()
            image
        }
}