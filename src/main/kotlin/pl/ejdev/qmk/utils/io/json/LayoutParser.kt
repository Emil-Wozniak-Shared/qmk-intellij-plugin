package pl.ejdev.qmk.utils.io.json

import com.beust.klaxon.JsonObject
import pl.ejdev.qmk.models.layouts.Layout
import pl.ejdev.qmk.models.layouts.LayoutCell
import pl.ejdev.qmk.models.layouts.LayoutMeta
import java.io.File

internal object LayoutParser {
    private const val NEW_LINE = "\n"
    private const val LAYOUTS = "layouts"
    private const val LAYOUT = "layout"

    fun parse(file: File, selected: String): List<Layout> =
        file
            .readLines()
            .joinToString(NEW_LINE)
            .toJsonObject()
            ?.let { jsonObject ->
                jsonObject.`object`(LAYOUTS)
                    .map
                    .map { (name, layout) ->
                        Layout(
                            name = name,
                            cells = (layout as JsonObject)
                                .list<JsonObject>(LAYOUT)
                                .map(LayoutCell::from),
                            active = name == selected
                        )
                    }
            }
            ?: emptyList()

    fun parseMeta(file: File) =
        file
            .readLines()
            .joinToString(NEW_LINE)
            .toJsonObject()
            ?.let(LayoutMeta::from)
            ?: LayoutMeta.EMPTY
}
