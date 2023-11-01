package pl.ejdev.qmk.model

import com.beust.klaxon.JsonObject
import pl.ejdev.qmk.utils.safeDouble

data class KeyboardCap(
    var matrix: List<Int> = listOf(),
    var x: Double = 0.0, // 1.5
    var y: Double = 0.0, // 0.375
    var h: Double = 1.0, // 1.5
    var w: Double = 1.0, // 1.5
) {
    companion object {
        fun from(json: JsonObject): KeyboardCap = KeyboardCap(
            matrix = json.array<Int>("matrix")?.value.orEmpty(),
            x = json.safeDouble("x"),
            y = json.safeDouble("y"),
            h = json.safeDouble("h"),
            w = json.safeDouble("w"),
        )
    }
}