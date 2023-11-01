package pl.ejdev.qmk.utils

import com.beust.klaxon.JsonObject

fun JsonObject.safeDouble(prop: String, fallback: String = "1.0") =
    (this[prop]
        ?.toString() ?: fallback)
        .toDouble()
fun JsonObject.safeText(prop: String, fallback: String = "") =
    (this[prop]?.toString() ?: fallback)