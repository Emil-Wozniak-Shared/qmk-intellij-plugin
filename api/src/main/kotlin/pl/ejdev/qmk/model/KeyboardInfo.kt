package pl.ejdev.qmk.model

import com.beust.klaxon.JsonObject
import pl.ejdev.qmk.utils.text

data class KeyboardInfo(
    val keyboard: String,
    val name: String,
    val maintainer: String,
    val layouts: Set<String>
) {
    companion object {
        fun from(keyboard: String, json: JsonObject): KeyboardInfo {
            val name: String = json.text("name")
            val maintainer = json.text("maintainer")
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
