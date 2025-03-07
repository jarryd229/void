package world.gregs.voidps.world.activity.skill.agility.course

import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.client.variable.start
import world.gregs.voidps.engine.entity.character.exactMove
import world.gregs.voidps.engine.entity.character.face
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.move.walkTo
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.chat.ChatType
import world.gregs.voidps.engine.entity.character.player.clearRenderEmote
import world.gregs.voidps.engine.entity.character.player.renderEmote
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.character.player.skill.exp.exp
import world.gregs.voidps.engine.entity.character.setAnimation
import world.gregs.voidps.engine.entity.obj.objectOperate
import world.gregs.voidps.engine.inject
import world.gregs.voidps.engine.queue.strongQueue
import world.gregs.voidps.type.Direction
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.Zone

val npcs: NPCs by inject()

objectOperate("Walk-across", "gnome_log_balance") {
    player.agilityCourse("gnome")
    player.start("input_delay", 8)
    player.strongQueue("agility_log_balance") {
        onCancel = {
            player.tele(2474, 3436)
        }
        npcs.gnomeTrainer("Okay get over that log, quick quick!", listOf(Zone(878901), Zone(878900), Zone(876852)))
        player.renderEmote = "rope_balance"
        player.walkTo(Tile(2474, 3429), noCollision = true, noRun = true)
        player.message("You walk carefully across the slippery log...", ChatType.Filter)
        pause(8)
        player.clearRenderEmote()
        player.agilityStage(1)
        player.exp(Skill.Agility, 7.5)
        player.message("... and make it safely to the other side.", ChatType.Filter)
    }
}

objectOperate("Climb-over", "gnome_obstacle_net") {
    player.agilityCourse("gnome")
    npcs.gnomeTrainer("Move it, move it, move it!", listOf(Zone(8768252), Zone(876853)))
    player.message("You climb the netting...", ChatType.Filter)
    player.setAnimation("climb_up")
    player.start("input_delay", 2)
    player.strongQueue("agility_netting", 2) {
        player.agilityStage(2)
        player.tele(player.tile.x.coerceIn(2471, 2476), 3424, 1)
        player.exp(Skill.Agility, 7.5)
    }
}

objectOperate("Climb", "gnome_tree_branch_up") {
    npcs.gnomeTrainer("That's it - straight up", listOf(Zone(5069109), Zone(5071157)))
    player.message("You climb the tree...", ChatType.Filter)
    player.setAnimation("climb_up")
    player.start("input_delay", 2)
    player.strongQueue("agility_branch", 2) {
        player.message("... to the platform above.", ChatType.Filter)
        player.agilityStage(3)
        player.tele(2473, 3420, 2)
        player.exp(Skill.Agility, 5.0)
    }
}

objectOperate("Walk-on", "gnome_balancing_rope") {
    npcs.gnomeTrainer("Come on scaredy cat, get across that rope!", Zone(9263413))
    player.walkTo(Tile(2483, 3420, 2), noCollision = true, noRun = true)
    player.start("input_delay", 7)
    player.renderEmote = "rope_balance"
    player.strongQueue("agility_rope_balance", 7) {
        player.agilityStage(4)
        player.clearRenderEmote()
        player.exp(Skill.Agility, 7.5)
        player.message("You passed the obstacle successfully.", ChatType.Filter)
    }
}

objectOperate("Walk-on", "gnome_balancing_rope_end") {
    player.walkTo(Tile(2477, 3420, 2), noCollision = true, noRun = true)
    player.start("input_delay", 7)
    player.renderEmote = "rope_balance"
    player.strongQueue("agility_rope_balance", 7) {
        player.clearRenderEmote()
        player.exp(Skill.Agility, 7.5)
        player.message("You passed the obstacle successfully.", ChatType.Filter)
    }
}

objectOperate("Climb-down", "gnome_tree_branch_down") {
    player.message("You climb the tree...", ChatType.Filter)
    player.setAnimation("climb_down")
    player.start("input_delay", 2)
    player.strongQueue("branch", 2) {
        player.agilityStage(5)
        player.tele(2486, 3420, 0)
        player.exp(Skill.Agility, 5.0)
    }
}

objectOperate("Climb-over", "gnome_obstacle_net_free_standing") {
    player.agilityCourse("gnome")
    npcs.gnomeTrainer("My Granny can move faster than you.", Zone(876854))
    player.message("You climb the netting.", ChatType.Filter)
    player.setAnimation("climb_up")
    player.start("input_delay", 2)
    player.strongQueue("agility_netting", 2) {
        player.agilityStage(6)
        val direction = target.tile.delta(player.tile).toDirection().vertical()
        player.tele(player.tile.add(direction.delta).add(direction.delta))
        player.exp(Skill.Agility, 7.5)
    }
}

objectOperate("Squeeze-through", "gnome_obstacle_pipe_*") {
    player.agilityCourse("gnome")
    player.strongQueue("agility_obstacle_pipe", 1) {
        onCancel = {
            player.tele(target.tile.addY(-1))
        }
        player.start("input_delay", 8)
        player.face(Direction.NORTH)
        player.message("You pull yourself through the pipes..", ChatType.Filter)
        pause()
        player.setAnimation("climb_through_pipe")
        player.exactMove(target.tile.addY(2))
        pause(4)
        player.face(Direction.NORTH)
        player.tele(target.tile.addY(3))
        player.setAnimation("climb_through_pipe", delay = 1)
        player.exactMove(target.tile.addY(6))
        pause(2)
        if (player.agilityStage == 6) {
            player.agilityStage = 0
            player.inc("gnome_course_laps")
            player.exp(Skill.Agility, 39.0)
        }
        player.exp(Skill.Agility, 7.5)
    }
}