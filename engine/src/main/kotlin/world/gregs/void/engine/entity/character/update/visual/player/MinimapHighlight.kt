package world.gregs.void.engine.entity.character.update.visual.player

import world.gregs.void.engine.entity.character.player.Player
import world.gregs.void.engine.entity.character.update.Visual

/**
 * @author GregHib <greg@gregs.world>
 * @since April 25, 2020
 */
data class MinimapHighlight(var highlighted: Boolean = false) : Visual

const val MINIMAP_HIGHLIGHT_MASK = 0x400

fun Player.flagMinimapHighlight() = visuals.flag(MINIMAP_HIGHLIGHT_MASK)

fun Player.getMinimapHighlight() = visuals.getOrPut(MINIMAP_HIGHLIGHT_MASK) { MinimapHighlight() }

var Player.minimapHighlight: Boolean
    get() = getMinimapHighlight().highlighted
    set(value) {
        getMinimapHighlight().highlighted = value
        flagMinimapHighlight()
    }