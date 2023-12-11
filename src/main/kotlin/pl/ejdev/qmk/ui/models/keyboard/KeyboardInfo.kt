package pl.ejdev.qmk.ui.models.keyboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class KeyboardInfo(
    @SerialName("bootloader")
    val bootloader: String? = "", // halfkay
    @SerialName("build")
    val build: Build? = Build(),
    @SerialName("community_layouts")
    val communityLayouts: List<String?> = listOf(),
    @SerialName("debounce")
    val debounce: Int = 0, // 30
    @SerialName("layouts")
    val layouts: Map<String?, Layout> = mapOf(),
    @SerialName("maintainer")
    val maintainer: String? = "", // ZSA via Drashna
    @SerialName("manufacturer")
    val manufacturer: String? = "", // ZSA Technology Labs
    @SerialName("processor")
    val processor: String? = "", // atmega32u4
    @SerialName("rgb_matrix")
    val rgbMatrix: RgbMatrix? = RgbMatrix(),
    @SerialName("rgblight")
    val rgblight: Rgblight? = Rgblight(),
    @SerialName("tapping")
    val tapping: Tapping? = Tapping(),
    @SerialName("url")
    val url: String? = "", // ergodox-ez.com
    @SerialName("usb")
    val usb: Usb? = Usb(),
) {
    @Serializable
    internal data class Build(
        @SerialName("debounce_type")
        val debounceType: String? = "" // sym_eager_pr
    )

    @Serializable
    internal data class Layout(
        val layout: List<LayoutRow> = emptyList()
    ) {
        @Serializable
        internal data class LayoutRow(
            @SerialName("h")
            val h: Double? = 0.0, // 1.5
            @SerialName("matrix")
            val matrix: List<Int> = listOf(),
            @SerialName("w")
            val w: Double? = 0.0, // 1.5
            @SerialName("x")
            val x: Double? = 0.0, // 1.5
            @SerialName("y")
            val y: Double? = 0.0 // 0.375
        )
    }

    @Serializable
    internal data class RgbMatrix(
        @SerialName("driver")
        val driver: String? = "" // is31fl3731
    )

    @Serializable
    internal data class Rgblight(
        @SerialName("animations")
        val animations: Animations = Animations(),
        @SerialName("brightness_steps")
        val brightnessSteps: Int = 0, // 12
        @SerialName("hue_steps")
        val hueSteps: Int = 0, // 12
        @SerialName("sleep")
        val sleep: Boolean? = false // true
    ) {
        @Serializable
        internal data class Animations(
            @SerialName("alternating")
            val alternating: Boolean? = false, // true
            @SerialName("breathing")
            val breathing: Boolean? = false, // true
            @SerialName("christmas")
            val christmas: Boolean? = false, // true
            @SerialName("knight")
            val knight: Boolean? = false, // true
            @SerialName("rainbow_mood")
            val rainbowMood: Boolean? = false, // true
            @SerialName("rainbow_swirl")
            val rainbowSwirl: Boolean? = false, // true
            @SerialName("rgb_test")
            val rgbTest: Boolean? = false, // true
            @SerialName("snake")
            val snake: Boolean? = false, // true
            @SerialName("static_gradient")
            val staticGradient: Boolean? = false, // true
            @SerialName("twinkle")
            val twinkle: Boolean? = false // true
        )
    }

    @Serializable
    internal data class Tapping(
        @SerialName("toggle")
        val toggle: Int = 0 // 1
    )

    @Serializable
    internal data class Usb(
        @SerialName("device_version")
        val deviceVersion: String? = "", // 0.0.1
        @SerialName("vid")
        val vid: String? = "" // 0x3297
    )

    @Serializable
    internal data class Ws2812(
        @SerialName("pin")
        val pin: String? = "" // D7
    )
}