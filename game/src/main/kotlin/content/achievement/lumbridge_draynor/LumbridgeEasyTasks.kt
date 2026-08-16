package content.achievement.lumbridge_draynor

import content.entity.combat.killer
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.inv.inventory

class LumbridgeEasyTasks : Script {

    init {
        objectOperate("Taunt-through", "railing_wizards_tower") { (target) ->
            val demon = NPCs.at(tile.regionLevel).sortedBy { it.tile.distanceTo(tile) }.firstOrNull { it.id.startsWith("lesser_demon") } ?: return@objectOperate
            demon.say("Graaaagh!")
            animDelay("emote_raspberry")
            set("come_in_here_and_say_that_task", true)
        }

        itemAdded("iron_ore", inventory = "inventory") {
            if (softTimers.contains("mining") && tile in Areas["al_kharid_mine"]) {
                set("iron_on_task", true)
            }
        }

        itemAdded("logs", inventory = "inventory") {
            if (softTimers.contains("woodcutting") && tile in Areas["lumbridge_swamp"])  {
                set("it_was_dead_already_task", true)
            }
        }

        itemRemoved("raw_rat_meat", inventory = "inventory") {
            if (inventory[it.index].id == "cooked_meat" && softTimers.contains("cooking") && tile in Areas["lumbridge_swamp"]) {
                set("ratatouille_task", true)
            }
        }

        npcDeath("giant_rat*") {
            val killer = killer
            if (killer !is Player) {
                return@npcDeath
            }
            if (killer.tile !in Areas["lumbridge_swamp"]) {
                return@npcDeath
            }
            killer["you_doity_rat_task"] = true
        }

        itemAdded("steel_bar", inventory = "inventory") {
            if (softTimers.contains("smelting") && tile in Areas["lumbridge_furnace"]) {
                set("belter_of_a_smelter_task", true)
            }
        }

        itemAdded("raw_pike", inventory = "inventory") {
            if (softTimers.contains("fishing") && tile in Areas["lumbridge_river_fishing_area"]) {
                set("and_it_was_this_big_task", true)
            }
        }

        itemAdded("water_rune", inventory = "inventory") {
            if (softTimers.contains("runecrafting")) {
                set("slippery_when_wet_task", true)
            }
        }

        interfaceOpened("bank") {
            if (tile in Areas["draynor_bank"]) {
                set("money_down_the_drayn_task", true)
            }
        }

    }
}
