package pl.ejdev.qmk.ui.models.layouts

internal data class LayoutSettings(
    val layout: Layout = Layout()
) {
     data class Layout(
         val file: String = "default",
         val selected: String = "LAYOUT"
     )
}
