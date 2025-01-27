package content.minigame.sorceress_garden

import content.entity.proj.shoot
import world.gregs.voidps.engine.client.ui.open
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.hunt.huntPlayer
import world.gregs.voidps.engine.queue.softQueue
import world.gregs.voidps.type.Direction
import content.entity.sound.playSound

huntPlayer("*_elemental_#") { npc ->
    val direction = npc.direction
    for (player in targets) {
        if (direction != Direction.NONE && direction != player.tile.delta(npc.tile).toDirection()) {
            continue // Skip players that aren't in-front or under.
        }
        player.softQueue("delay", 0) {
            npc.anim("elemental_Pointing")
            player.playSound("stun_all")
            npc.shoot("curse", player.tile)
            player.gfx("curse_hit")
            player.open("fade_out")
            delay(2)
            player.tele(2911, 5470)
            player.open("fade_in")
            player.playSound("stunned")
        }
    }
}