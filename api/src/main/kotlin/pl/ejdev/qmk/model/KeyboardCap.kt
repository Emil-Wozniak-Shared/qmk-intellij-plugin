package pl.ejdev.qmk.model

import com.beust.klaxon.JsonObject

data class KeyboardCap(
    var matrix: List<Int>? = listOf(),
    var x: Double? = 0.0, // 1.5
    var y: Double? = 0.0, // 0.375
    var h: Double? = 0.0, // 1.5
    var w: Double? = 0.0, // 1.5
) {
    companion object {
        fun from(json: JsonObject): KeyboardCap = KeyboardCap(
             matrix = json.array<Int>("matrix")?.value,
             x = json["x"]?.toString()?.toDouble(),
             y = json["y"]?.toString()?.toDouble(),
             h = json["h"]?.toString()?.toDouble(),
             w = json["w"]?.toString()?.toDouble(),
         )
    }
}