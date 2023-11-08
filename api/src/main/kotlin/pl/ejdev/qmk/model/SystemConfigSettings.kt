package pl.ejdev.qmk.model

data class SystemConfigSettings(
    val keyboard: String,
    val layout: String,
    val layers: KeyboardLayers
)
