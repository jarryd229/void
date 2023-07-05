package world.gregs.voidps.world.activity.skill.crafting

import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.client.variable.clear
import world.gregs.voidps.engine.client.variable.inc
import world.gregs.voidps.engine.contain.inventory
import world.gregs.voidps.engine.contain.remove
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.world.activity.skill.ItemOnItem

on<ItemOnItem>({ def.skill == Skill.Crafting && def.requires.any { it.id == "thread" } }) { player: Player ->
    val value = player.inc("thread_used")
    if (value == 5) {
        player.clear("thread_used")
        player.inventory.remove("thread")
        player.message("You use up one of your reels of thread.")
    }
}