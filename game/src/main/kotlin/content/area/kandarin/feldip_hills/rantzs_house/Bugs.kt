package content.area.kandarin.feldip_hills.rantzs_house

import content.entity.player.dialogue.Neutral
import content.entity.player.dialogue.type.npc
import content.quest.quest
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.entity.character.player.Player


class Bugs : Script {

    init {
        npcOperate("Talk", "bugs_rantzs_house") {
            when (quest("big_chompy_bird_hunting")) {
                "unstarted", "started" -> {
                    npc<Neutral>("You's better talk to Dad, him chasey sneaky da chompy.")
                }
                "stage" -> stage()
                else -> completed()
            }
        }
    }


    suspend fun Player.stage() {
    }

    suspend fun Player.completed() {
    }

}
