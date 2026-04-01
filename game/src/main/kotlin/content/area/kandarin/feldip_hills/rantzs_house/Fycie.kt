package content.area.kandarin.feldip_hills.rantzs_house

import content.entity.player.dialogue.*
import content.entity.player.dialogue.type.*
import content.quest.quest
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.item.floor.FloorItems
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.engine.inv.remove

class Fycie : Script {

    init {
        playerSpawn {
        //    sendVariable("brought_feathers_from_fycie")
        }

        npcOperate("Talk", "fycie_rantzs_house") {
            when (quest("big_chompy_bird_hunting")) {
                "unstarted" -> {
                    npc<Neutral>("You's better talk to Dad, We not talk to wierdly 'umans.")
                }
                "started" -> started()
                else -> completed()
            }
        }
    }

    suspend fun Player.started() {
        if (get("brought_feathers_from_fycie", false)) {
            npc<Neutral>("I's sorry creature, I don't got no more flufsies! But I got lots of bright pretties! Hope you's get da stabbers for Dad soonly!")
        } else {
            npc<Neutral>("Hey you Creature, I know's what you is You's a 'uman!")
            player<Happy>("That's right... and I'm making some 'stabbers' for Rantz.")
            npc<Neutral>("Dat's great...Dad want's to hunt da chompy... Da chompy is our bestest yumms! You needsing flufsies for stabbers, Fycie have some but I is wanting some bright pretties for em!")
            item("feather", 400, "Fycie shows you the flufsies...you count 25 of them.")
            player<Neutral>("How many 'bright pretties' do you want?")
            npc<Neutral>("Mee's wants lots of bright pretties, this many! <col=0000ff>~ Fycie <col=0000ff>quickly opens and closes her hands in front ~ <col=0000ff>~ of you <col=0000ff>to indicate a number of bright pretties. ~ <col=0000ff>~ It looks like <col=0000ff>she wants 50 gold coins.~ ")
            choice {
                option<Sad>("Ok, I'll give you 50 bright pretties.") {
                    if (inventory.remove("coins", 50)) {
                        if (!inventory.add("feather", 25)) {
                            FloorItems.add(tile, "feather", 25, disappearTicks = 300, owner = this)
                        }
                        set("brought_feathers_from_fycie", true)
                        items(item1 = "coins_8", item2 = "feather", "You offer the 50 coins for the 25 flufsies.")
                    } else {
                        npc<Sad>("You's not got da bright pretties... I wants da bright pretties..you not get no flufsies wid'out da bright pretties.")
                    }
                }
                option<Neutral>("Er, sorry, I can't give you that many...") {
                    npc<Sad>("Well, you not have da flufsies den!")
                }
            }
        }
    }

    suspend fun Player.completed() {
    }
}
