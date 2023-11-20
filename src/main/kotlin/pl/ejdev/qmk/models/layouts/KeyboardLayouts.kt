package pl.ejdev.qmk.models.layouts

import com.beust.klaxon.JsonObject
import pl.ejdev.qmk.utils.io.json.safeDouble
import pl.ejdev.qmk.utils.io.json.text

internal data class KeyboardLayouts(
    val filename: String,
    val meta: LayoutMeta,
    val layouts: List<Layout>,
    val active: Boolean
) {
    val rawName = filename.replace(".json", "")
}

internal data class LayoutMeta(
    val keyboardName: String?,
    val manufacturer: String?,
    val maintainer: String?,
    val url: String?,
    val communityLayouts: List<String>
) {
    companion object {
        fun from(jsonObject: JsonObject) =
            LayoutMeta(
                keyboardName = jsonObject.text("keyboard_name"),
                manufacturer = jsonObject.text("manufacturer"),
                maintainer = jsonObject.text("maintainer"),
                url = jsonObject.text("url"),
                communityLayouts = jsonObject.array<String>("community_layouts").orEmpty()
            )

        val EMPTY = LayoutMeta(null, null, null, null, emptyList())
    }
}

internal data class Layout(
    val name: String,
    val cells: List<LayoutCell>,
    val active: Boolean
)

internal data class LayoutCell(
    var matrix: List<Int> = listOf(),
    var x: Double = 0.0,
    var y: Double = 0.0,
    var h: Double = 1.0,
    var w: Double = 1.0,
) {
    companion object {
        fun from(json: JsonObject): LayoutCell = LayoutCell(
            matrix = json.array<Int>("matrix")?.value.orEmpty(),
            x = json.safeDouble("x"),
            y = json.safeDouble("y"),
            h = json.safeDouble("h"),
            w = json.safeDouble("w"),
        )
    }
}
