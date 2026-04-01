package content.quest.member.big_chompy_bird_hunting


import content.entity.combat.killer
import content.entity.player.dialogue.type.item
import content.quest.quest
import content.quest.questJournal
import net.pearx.kasechange.toLowerSpaceCase
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.data.definition.Areas
import world.gregs.voidps.engine.entity.character.mode.interact.ItemOnNPCInteract
import world.gregs.voidps.engine.entity.character.mode.interact.ItemOnObjectInteract
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.Players
import world.gregs.voidps.engine.entity.character.player.chat.noInterest
import world.gregs.voidps.engine.entity.item.floor.FloorItem
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.engine.inv.replace
import world.gregs.voidps.type.Direction
import world.gregs.voidps.type.Tile
import kotlin.text.replace


class BigChompyBirdHunting : Script {

    init {
        playerSpawn {
            // sendVariable("big_chompy_bird_hunting")
        }

        floorItemSpawn("bloated_toad") {
            val player = Players.find(owner ?: return@floorItemSpawn) ?: return@floorItemSpawn
            if (player.tile !in Areas["feldip_hills_chompy_bird_hunting_area"]) {
                return@floorItemSpawn
            }
            player.message("You carefully place the bloated toad bait.")
            val chompyBird = NPCs.add("chompy_bird", Tile(2636, 2966))// for testing 
        }

        itemOnObjectOperate("ogre_bellows", "swamp_bubbles", handler = ::fillBellows)
        itemOnObjectOperate("ogre_bellows_1", "swamp_bubbles", handler = ::fillBellows)
        itemOnObjectOperate("ogre_bellows_2", "swamp_bubbles", handler = ::fillBellows)

        itemOnNPCOperate("ogre_bellows_3", "swamp_toad_feldip_hills", handler = ::catchToad)

        itemOnItem("knife", "achey_tree_logs") { _, _ ->
            if (quest("big_chompy_bird_hunting") == "unstarted") {
                message("You're not sure what you would make with these logs.")
                return@itemOnItem
            }
        }

        questJournalOpen("big_chompy_bird_hunting") {
            val lines = when (quest("big_chompy_bird_hunting")) {
                "started" -> {
                    listOf(
                        "<str>I found a ogre named Rantz near a cave just East of the",
                        "<str>Ogre City Gu'Tonoth. When I spoke to him he seemed",
                        "<str>obsessed with using things called 'stabbers' to hunt",
                        "<str>'Chompy Birds'.",
                        "<navy>I agreed to get <maroon>Rantz <navy>some <maroon>'stabbers'",
                    )
                }
                else -> listOf(
                    "<navy>I can start this quest by talking to <maroon>Rantz <navy>south east of <maroon>Gu'Tanoth",
                    "",
                    "",
                )
            }
            questJournal("Big Chompy Bird Hunting", lines)
        }

        objectOperate("Unlock", "locked_ogre_chest_rantzs_house") {
            message("Perhaps you'd better ask permission before opening this.")
        }
    }

    fun fillBellows(player: Player, interact: ItemOnObjectInteract) {
        val required = interact.item.id
        player.gfx("241")
        player.anim("1026")
        player.inventory.replace(required,"ogre_bellows_3") //-1 item id
        player.message("You collect some gas from the swamp.")
    }

    fun catchToad(player: Player, interact: ItemOnNPCInteract) {
        val required = interact.item.id
        if (player.inventory.isFull()) {
            player.message("You don't have space to carry that.")
        }
        player.say("Come here toady!")
        player.gfx("241")
        player.anim("1026")
        player.inventory.replace(required,"ogre_bellows")//-1 item id
        player.inventory.add("bloated_toad")
        player.message("You add the bloated toad to your inventory.")
    }

}
