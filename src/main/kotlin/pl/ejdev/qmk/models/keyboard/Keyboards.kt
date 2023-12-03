package pl.ejdev.qmk.models.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Keyboards(
    @SerialName("keyboards")
    val keyboards: List<Keyboard> = listOf()
)

@Serializable
internal data class Keyboard(
    @SerialName("keyboard_name")
    val keyboardName: String? = "", // Emi20
    @SerialName("maintainer")
    val maintainer: String? = "", // Aquacylinder
    @SerialName("manufacturer")
    val manufacturer: String? = "", // Aquacylinder
    @SerialName("path")
    val path: String? = "" // ./keyboards/emi20/info.json
)
