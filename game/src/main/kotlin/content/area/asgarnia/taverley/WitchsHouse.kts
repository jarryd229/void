package content.area.asgarnia.taverley

import content.entity.proj.shoot
import content.entity.sound.playSound
import org.rsmod.game.pathfinder.LineValidator
import world.gregs.voidps.engine.client.variable.start
import world.gregs.voidps.engine.data.definition.PatrolDefinitions
import world.gregs.voidps.engine.entity.character.mode.Patrol
import world.gregs.voidps.engine.entity.character.mode.move.hasLineOfSight
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.hunt.huntPlayer
import world.gregs.voidps.engine.entity.npcSpawn
import world.gregs.voidps.engine.inject
import world.gregs.voidps.engine.queue.softQueue
import world.gregs.voidps.type.Direction

val patrols: PatrolDefinitions by inject()
val lineValidator: LineValidator by inject()

npcSpawn("nora_t_hagg") { npc ->
    val patrol = patrols.get("nora_t_hagg")
    npc.mode = Patrol(npc, patrol.waypoints)
}

huntPlayer("nora_t_hagg") { npc ->
    val facing = npc.direction
    for (player in targets) {
        val direction = player.tile.delta(npc.tile).toDirection()
        if (direction != Direction.SOUTH && direction != Direction.NORTH && direction != facing.rotate(1) && direction != facing.rotate(-1)) {
            continue // Skip players that aren't within 180 degrees of the direction nora is facing
        }
        if (!lineValidator.hasLineOfSight(player, npc)) {
            continue // Check sight in both directions so nora can't see players standing on bush corners
        }
        player.start("movement_delay", 4)
        npc.face(player)
        npc.say("Stop! Thief")
        //todo add delay it skips "Stop! Thief"
        npc.say("Klarata... Sepptento... Valkan!")
        npc.gfx("curse_cast")
        player.playSound("curse_cast_and_fire")
        if (!npc.contains("old_model")) {
            npc.anim("curse")
        }
        npc.shoot("curse", player.tile)
        player.playSound("curse_hit")
        player.softQueue("delay", 2) {
            player.gfx("curse_hit")
        }
        player.softQueue("teleport", 2) {
            player.tele(2928, 3455)
        }
    }
}