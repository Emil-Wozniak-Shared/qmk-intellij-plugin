package pl.ejdev.qmk.model

import com.beust.klaxon.JsonObject
import pl.ejdev.qmk.utils.safeText

data class KeyboardInfo(
    val keyboard: String,
    val name: String,
    val maintainer: String,
    val layouts: Set<String>
) {
    companion object {
        fun from(keyboard: String, json: JsonObject): KeyboardInfo {
            val name: String = json.safeText("name")
            val maintainer = json.safeText("maintainer")
            val layouts = json["layouts"].toString().split(",").toSet()
            return KeyboardInfo(
                keyboard,
                name,
                maintainer,
                layouts
            )
        }
    }
}
