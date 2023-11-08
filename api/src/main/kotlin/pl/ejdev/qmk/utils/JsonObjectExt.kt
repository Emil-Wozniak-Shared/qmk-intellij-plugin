package pl.ejdev.qmk.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject

fun JsonObject.safeDouble(prop: String, fallback: String = "1.0") =
    (this[prop]
        ?.toString() ?: fallback)
        .toDouble()
fun JsonObject.text(prop: String, fallback: String = "") =
    (this[prop]?.toString() ?: fallback)

@Suppress("UNCHECKED_CAST")
fun<T> JsonObject.list(prop: String) =
    (this[prop] as JsonArray<T>?)?.value.orEmpty()