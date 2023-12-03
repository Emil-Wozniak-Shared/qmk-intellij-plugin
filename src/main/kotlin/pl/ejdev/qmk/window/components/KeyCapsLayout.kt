package pl.ejdev.qmk.window.components

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBPanel
import pl.ejdev.qmk.models.layouts.LayoutCell
import pl.ejdev.qmk.utils.io.csv.KeyCode
import pl.ejdev.qmk.utils.io.csv.toGraphic
import pl.ejdev.qmk.window.ui.DEJAVU_14_BOLD
import pl.ejdev.qmk.window.ui.KEYCAP_DARK
import pl.ejdev.qmk.window.ui.KEYCAP_LIGHT
import java.awt.*
import java.awt.font.FontRenderContext
import javax.swing.Icon
import javax.swing.ImageIcon
import kotlin.math.roundToInt

internal class KeyCapsLayout(
    private val width: Int = 100,
    private val height: Int = 100,
    private val labels: List<KeyCode>,
    private val visible: Boolean = true
) : JBPanel<DialogPanel>(FlowLayout()) {
    constructor(
        width: Double,
        groups: Map<List<Int>, List<LayoutCell>>,
        height: Double,
        codes: List<List<KeyCode>>,
        capSize: Int,
        layerIndex: Int
    ) : this(
        width = (width * (capSize * capSize) * groups.maxOf { it.value.size }).roundToInt(),
        height = (height * (capSize * capSize) * groups.size).roundToInt(),
        labels = codes[layerIndex],
        visible = layerIndex == 0
    )

    private val keymap: MutableList<Rectangle> = mutableListOf()

    init {
        isVisible = visible
    }

    private fun rectangle(cell: LayoutCell, size: Int): Rectangle {
        val x: Int = (cell.x * size).roundToInt()
        val y: Int = (cell.y * size).roundToInt()
        val w: Int = (cell.w * size).roundToInt()
        val h: Int = (cell.h * size).roundToInt()
        return Rectangle(x, y, w, h)
    }

    internal fun addKeymaps(values: Collection<List<LayoutCell>>, capSize: Int) = apply {
        values.map { cellGroups ->
            cellGroups.forEach { cell ->
                val shape = rectangle(cell, capSize)
                keymap.add(shape)
            }
        }
    }

    override fun getPreferredSize(): Dimension = Dimension(width, height)

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        keymap.onEachIndexed { index, rectangle ->
            val isLabels = labels.isNotEmpty() && labels.size > index
            val isNOOP = if (isLabels) labels[index].isNOOP() else false
            (graphics as Graphics2D).apply {
                drawBox(rectangle, JBColor.WHITE, 0)
                drawBox(rectangle, background = if (isNOOP) JBColor.DARK_GRAY else KEYCAP_DARK, padding = 1)
                drawBox(rectangle, background = if (isNOOP) JBColor.LIGHT_GRAY else KEYCAP_LIGHT, padding = 4)

                if (isLabels) {
                    color = Gray._255
                    labels[index].toGraphic(
                        onSpecialKey = { graphics.drawIcon(it, rectangle) },
                        onDefault = { centerString(rectangle = rectangle, text = it, font = DEJAVU_14_BOLD) }
                    )
                }
            }
        }
    }

    private fun Graphics.drawIcon(icon: Icon, rectangle: Rectangle) {
        drawImage(
            icon.iconToImage(),
            rectangle.x + 16,
            rectangle.y + 16,
            rectangle.width - 24,
            rectangle.height - 24,
            this@KeyCapsLayout
        )
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

    private fun Graphics.centerString(
        rectangle: Rectangle,
        text: String,
        font: Font
    ) {
        val frc = FontRenderContext(null, true, true)
        val r2D = font.getStringBounds(text, frc)
        val rWidth = Math.round(r2D.width).toInt()
        val rHeight = Math.round(r2D.height).toInt()
        val rX = Math.round(r2D.x).toInt()
        val rY = Math.round(r2D.y).toInt()
        val a = rectangle.width / 2 - rWidth / 2 - rX
        val b = rectangle.height / 2 - rHeight / 2 - rY
        this.font = font
        this.drawString(text, rectangle.x + a, rectangle.y + b)
    }

    private fun Icon.iconToImage(x: Int = 0, y: Int = 0): Image =
        if (this is ImageIcon) image
        else {
            val image = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .defaultScreenDevice
                .defaultConfiguration.createCompatibleImage(iconWidth, iconHeight)
            val graphics2D = image.createGraphics()
            paintIcon(null, graphics2D, x, y)
            graphics2D.dispose()
            image
        }
}