package world.gregs.voidps.tools.map.view.ui

import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JPanel

class AreaPointSettings : JPanel() {
    val coords = CoordinatesPane("Coordinates")

    init {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        coords.zCoord.isVisible = false
        add(coords)
    }
}
