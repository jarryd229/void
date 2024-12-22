package world.gregs.voidps.world.activity.minigames.sorceress

import world.gregs.voidps.engine.client.ui.open
import world.gregs.voidps.engine.entity.character.facing
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.hunt.huntPlayer
import world.gregs.voidps.engine.entity.character.setGraphic
import world.gregs.voidps.engine.queue.softQueue
import world.gregs.voidps.type.Direction
import world.gregs.voidps.world.interact.entity.sound.playSound

huntPlayer("*_elemental_#") { npc ->
    val direction = npc.facing
    for (player in targets) {
        if (direction != Direction.NONE && direction != player.tile.delta(npc.tile).toDirection()) {
            continue // Skip players that aren't in-front or under.
        }
        player.softQueue("delay", 2) {
            player.playSound("1930")
            player.setGraphic("188")
            player.open("fade_out")
            player.softQueue("teleport", 2) {
                player.tele(2911, 5470)
                player.open("fade_in")
            }
        }
    }
}