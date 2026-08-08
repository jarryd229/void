package content.area.asgarnia.goblin_village

import content.entity.player.dialogue.*
import content.entity.player.dialogue.type.ChoiceOption
import content.entity.player.dialogue.type.choice
import content.entity.player.dialogue.type.npc
import content.entity.player.dialogue.type.player
import content.entity.player.dialogue.type.startQuest
import content.quest.quest
import content.quest.refreshQuestJournal
import content.quest.setInstanceLogout
import content.quest.startCutscene
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.client.moveCamera
import world.gregs.voidps.engine.client.turnCamera
import world.gregs.voidps.engine.client.ui.open
import world.gregs.voidps.engine.entity.character.mode.PauseMode
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.Direction
import world.gregs.voidps.type.Region
import world.gregs.voidps.type.Tile
import world.gregs.voidps.type.random

class Goblins : Script {

    init {
        npcOperate("Talk-to", "goblin_*_red") {
            when (random.nextInt(0, 2)) {
                0 -> {
                    npc<Idle>("Red armour best!")
                    choice {
                        option<Confused>("Why is red best?") {
                            npc<Scared>("Cos General Bentnoze says so, and he bigger than me.")
                        }
                        option<Confused>("Err, okay.")
                    }
                }
                1 -> {
                    player<Quiz>("Why are you fighting?")
                    npc<Frustrated>("He wearing green armour! General Bentnoze tell us wear red!")
                    npc<Frustrated>(npcId = "goblin_staff_green", "But General Wartface say we must wear green!")
                }
            }
        }

        npcOperate("Talk-to", "goblin_*_green") {
            when (random.nextInt(0, 2)) {
                0 -> {
                    npc<Idle>("green armour best!")
                    choice {
                        option<Confused>("Why is green best?") {
                            npc<Scared>("Cos General Wartface says so, and he bigger than me.")
                        }
                        option<Confused>("Err, okay.")
                    }
                }
                1 -> {
                    player<Quiz>("Why are you fighting?")
                    npc<Frustrated>("He wearing red armour! General Wartface tell us wear green!")
                    npc<Frustrated>(npcId = "goblin_shield_battleaxe_red", "But General Bentnoze say we must wear red!")
                }
            }
        }

        npcOperate("Talk-to", "grubfoot*") {
            npc<Disheartened>("Grubfoot wear red armour! Grubfoot wear green armour!")
            npc<Quiz>("Why they not make up their minds?")
            npc<Frustrated>(npcId = "general_bentnoze_rfd", "Shut up Grubfoot!")
        }

        npcOperate("Talk-to", "general_wartface_goblin_village,general_bentnoze_goblin_village") {
            when (quest("goblin_diplomacy")) {
                "completed" -> {

                }
                else -> talking()
            }
        }
    }

    suspend fun Player.talking() {
        when (random.nextInt(0, 3)) {
            0 -> {
                npc<Neutral>(npcId = "general_bentnoze_rfd","All goblins should wear red armour!")
                npc<Scared>(npcId = "general_wartface_rfd","Not red! Red armour make you look fat.")
                npc<Happy>(npcId = "general_bentnoze_rfd","Everything make YOU look fat!")
                npc<Angry>(npcId = "general_wartface_rfd","Shut up!")
                npc<Happy>(npcId = "general_bentnoze_rfd","Fatty!")
                npc<Angry>(npcId = "general_wartface_rfd","SHUT UP!")
                npc<Quiz>(npcId = "general_bentnoze_rfd","Even this human think you look fat! Don't you, human?")
                player<Confused>("Um...")
                choice {
                    looksFat()
                    doesntLookFat()
                    leaveYouToIt()
                }
            }
            1 -> {
                npc<Angry>(npcId = "general_bentnoze_rfd","Red armour best.")
                npc<Angry>(npcId = "general_wartface_rfd","No it has to be green!")
                npc<Neutral>(npcId = "general_bentnoze_rfd","Go away human, we busy.")
                menu()
            }
            2 -> {
                npc<Neutral>(npcId = "general_wartface_rfd","We should wear green armour!")
                npc<Quiz>(npcId = "general_bentnoze_rfd","Green armour? Are you stupid?")
                npc<Angry>(npcId = "general_wartface_rfd","You stupid! Only stupid goblins think red armour better!")
                npc<Angry>(npcId = "general_bentnoze_rfd","No they don't! Me think red armour better!")
                npc<Happy>(npcId = "general_wartface_rfd","That because you stupid!")
                npc<Angry>(npcId = "general_bentnoze_rfd","Me not stupid!")
                npc<Quiz>(npcId = "general_wartface_rfd","Then why you not like green armour?")
                npc<Angry>(npcId = "general_bentnoze_rfd","Because red armour better!")
                npc<Happy>(npcId = "general_wartface_rfd","Only stupid goblins think that! You stupid!")
                menu()
            }
        }
    }


    suspend fun Player.menu() {
        choice {
            if (quest("goblin_diplomacy") == "started") {
                if (inventory.contains("orange_goblin_mail")) {
                    iHaveOrangeArmour()
                } else {
                    whereDoIGetOrangeArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_orange") {
                if (inventory.contains("blue_goblin_mail")) {

                } else {
                    whereDoIGetBlueArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_blue") {
                if (inventory.contains("goblin_mail")) {

                } else {
                    whereDoIGetBrownArmour()
                }
            }
            arguing()
            preferPeace()
            if (quest("goblin_diplomacy") == "unstarted") {
                pickAnArmour()
            }
            leaveYouToIt()
        }
    }

    fun ChoiceOption.looksFat() = option<Laugh>("Yes, he looks fat!") {
        npc<Happy>(npcId = "general_bentnoze_rfd","Ha ha! See, fatty? Even human think you fat!")
        npc<Angry>(npcId = "general_wartface_rfd","Me not care what human think! Human ugly!")
        menu()
    }

    fun ChoiceOption.doesntLookFat() = option<Neutral>("No, he doesn't look fat.") {
        npc<Angry>(npcId = "general_bentnoze_rfd","Shut up human! Wartface fat and human stupid!")
        npc<Angry>(npcId = "general_wartface_rfd","Shut up Bentnoze!")
        menu()
    }

    fun ChoiceOption.arguing(): Unit = option<Confused>("Why are you arguing about the colour of your armour?") {
        npc<Neutral>(npcId = "general_bentnoze_rfd","We decide to celebrate goblin new century by changing colour of our armour. Brown get boring after a bit. We want change.")
        npc<Neutral>(npcId = "general_wartface_rfd","Problem is, we can't agree on new colour. We think maybe we use old goblin tribe colours, but Bentnoze from rubbish Thorobshuun tribe and me from much better Garagorshuun tribe.")
        npc<Happy>(npcId = "general_bentnoze_rfd","Thorobshuun tribe colour is red! Way better than silly green of Garagorshuun tribe.")
        npc<Angry>(npcId = "general_wartface_rfd","Shut up!")
        choice {
            if (quest("goblin_diplomacy") == "started") {
                if (inventory.contains("orange_goblin_mail")) {
                    iHaveOrangeArmour()
                } else {
                    whereDoIGetOrangeArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_orange") {
                if (inventory.contains("blue_goblin_mail")) {

                } else {
                    whereDoIGetBlueArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_blue") {
                if (inventory.contains("goblin_mail")) {

                } else {
                    whereDoIGetBrownArmour()
                }
            }
            goblinNewCentury()
            preferPeace()
            if (quest("goblin_diplomacy") == "unstarted") {
                pickAnArmour()
            }
            leaveYouToIt()
        }
    }

    fun ChoiceOption.preferPeace() = option<Confused>("Wouldn't you prefer peace?") {
        npc<Neutral>(npcId = "general_wartface_rfd","Goblins not so good at peace. Maybe peace be okay though, as long as it peace wearing green armour.")
        npc<Neutral>(npcId = "general_bentnoze_rfd","But green too much like skin. Nearly make you look naked!")
        choice {
            if (quest("goblin_diplomacy") == "started") {
                if (inventory.contains("orange_goblin_mail")) {
                    iHaveOrangeArmour()
                } else {
                    whereDoIGetOrangeArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_orange") {
                if (inventory.contains("blue_goblin_mail")) {

                } else {
                    whereDoIGetBlueArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_blue") {
                if (inventory.contains("goblin_mail")) {

                } else {
                    whereDoIGetBrownArmour()
                }
            }
            arguing()
            if (quest("goblin_diplomacy") == "unstarted") {
                pickAnArmour()
            }
            leaveYouToIt()
        }
    }

    fun ChoiceOption.pickAnArmour() = option<Confused>("Do you want me to pick an armour colour for you?") {
        npc<Neutral>(npcId = "general_wartface_rfd","Yes, as long as you pick green.")
        npc<Neutral>(npcId = "general_bentnoze_rfd","No you have to pick red!")
        choice {
            wearRed()
            wearGreen()
            differentColour()
            leaveYouToIt()
        }
    }

    fun ChoiceOption.wearRed() = option<Happy>("You should wear red.") {
        npc<Happy>(npcId = "general_bentnoze_rfd","See? Even stupid human think red best. Now we all wear red!")
        npc<Angry>(npcId = "general_wartface_rfd","Human not know anything! If we wear red then whole village be ugly like YOU!")
        npc<Neutral>(npcId = "general_bentnoze_rfd","Go away human. You not helping.")
        choice {
            differentColour()
            leaveYouToIt()
        }
    }

    fun ChoiceOption.wearGreen() = option<Happy>("You should wear green.") {
        npc<Happy>(npcId = "general_wartface_rfd","Green! We all wear green now, human has decided!")
        npc<Quiz>(npcId = "general_bentnoze_rfd","Why we have to do what human say? He not boss of us!")
        npc<Happy>(npcId = "general_wartface_rfd","No but he agree with me!")
        npc<Angry>(npcId = "general_bentnoze_rfd","That prove you a filthy human-lover!")
        npc<Angry>(npcId = "general_wartface_rfd","Me hate humans! This human just happen to be right!")
        npc<Angry>(npcId = "general_bentnoze_rfd","Go away human. You not know anything.")
        choice {
            differentColour()
            leaveYouToIt()
        }
    }

    fun ChoiceOption.differentColour() = option<Quiz>("What about a different colour?") {
        npc<Confused>(npcId = "general_bentnoze_rfd","That would mean me wrong... but at least Wartface not right!")
        npc<Confused>(npcId = "general_wartface_rfd","Well Bentnoze never been right in his life. Still, maybe new colour good, but will have to see armour before decide.")
        npc<Happy>(npcId = "general_bentnoze_rfd","Human! You bring us armour in new colour!")
        if (startQuest("goblin_diplomacy")) {
            player<Happy>("I can do that.")
            npc<Quiz>(npcId = "general_wartface_rfd","What colour we try?")
            set("goblin_diplomacy", "started")
            refreshQuestJournal()
            npc<Neutral>(npcId = "general_bentnoze_rfd","Orange armour might be good.")
            npc<Neutral>(npcId = "general_wartface_rfd","Yep, bring us orange armour.")
            whereDoIGetOrangeArmour()
        } else {
            player<Neutral>("Actually, I think I'll leave you to it.")
        }
    }


     fun ChoiceOption.whereDoIGetOrangeArmour() = option<Quiz>("How am I meant to get orange armour?") {
        npc<Neutral>(npcId = "general_bentnoze_rfd","Well first you get goblin armour...")
        npc<Happy>(npcId = "general_wartface_rfd","...and then you dye it orange!")
        npc<Happy>(npcId = "general_bentnoze_rfd","Even human should be able to work that out!")
        choice {
            whereDoIGetArmour()
            whereDoIGetDyer()
            option<Neutral>("Okay, I'll be back soon.")
        }
    }

    fun ChoiceOption.whereDoIGetBlueArmour() = option<Quiz>("How am I meant to get blue armour?") {
        npc<Neutral>(npcId = "general_bentnoze_rfd","Maybe same way you got orange armour?")
        npc<Happy>(npcId = "general_wartface_rfd","Get goblin armour and dye it blue!")
        choice {
            whereDoIGetArmour()
            whereDoIGetDyer()
            option<Neutral>("Okay, I'll be back soon.")
        }
    }

    fun ChoiceOption.whereDoIGetBrownArmour() = option<Quiz>("How am I meant to get brown armour?") {
        npc<Neutral>(npcId = "general_wartface_rfd","Brown was old colour of armour before we change it.")
        npc<Neutral>(npcId = "general_bentnoze_rfd","There bound to be some around somewhere.")
        player<Neutral>("Okay, I'll be back soon.")
    }

    fun ChoiceOption.whereDoIGetArmour() = option<Quiz>("Where do I get goblin armour?") {
        npc<Neutral>(npcId = "general_wartface_rfd","There some spare armour around village somewhere. You can take that.")
        //set varbit gobdip_know_about_armour
        npc<Neutral>(npcId = "general_bentnoze_rfd","It in crates somewhere. Can't remember which crates now.")
        choice {
            whereDoIGetDyer()
            option<Neutral>("Okay, I'll be back soon.")
        }
    }

    fun ChoiceOption.whereDoIGetDyer(): Unit = option<Quiz>("Where do I get dye?") {
        npc<Happy>(npcId = "general_bentnoze_rfd","You go north of here into wilderness. There you find many ways to die!")
        player<Bored>("No, D-Y-E, not D-I-E.")
        npc<Happy>(npcId = "general_wartface_rfd","Stupid Bentnoze, you not know how to spell!")
        npc<Angry>(npcId = "general_bentnoze_rfd","Shut up Wartface!")
        player<Quiz>("Do you know where I can get dye?")
        npc<Neutral>(npcId = "general_bentnoze_rfd","Me not know where dye come from.")
        player<Quiz>("Well where did you get your red and green dye from?")
        //set varbit gobdip_know_about_dye
        npc<Neutral>(npcId = "general_wartface_rfd","Some goblin or other, he steal it. Say he steal it from old witch in Draynor Village.")
        npc<Quiz>(npcId = "general_bentnoze_rfd","Maybe you can get more dye from her?")
        choice {
            whereDoIGetArmour()
            option<Neutral>("Okay, I'll be back soon.")
        }
    }

    fun ChoiceOption.leaveYouToIt() = option<Neutral>("I'll leave you to it.") {
    }

    fun ChoiceOption.goblinNewCentury() = option<Quiz>("What is the goblin new century?") {
        npc<Neutral>(npcId = "general_bentnoze_rfd","Goblin century mark year of battle on Plain of Mud. That when Big High War God give goblin commandments.")
        player<Quiz>("Big High War God?")
        npc<Happy>(npcId = "general_wartface_rfd","Big High War God is god for goblins. He take us and make us strong, not stupid like humans.")
        npc<Neutral>(npcId = "general_bentnoze_rfd","Me bet Big High War God would pick red armour.")
        npc<Angry>(npcId = "general_wartface_rfd","Shut up! Big High War God would hate you!")
        choice {
            if (quest("goblin_diplomacy") == "started") {
                if (inventory.contains("orange_goblin_mail")) {
                    iHaveOrangeArmour()
                } else {
                    whereDoIGetOrangeArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_orange") {
                if (inventory.contains("blue_goblin_mail")) {

                } else {
                    whereDoIGetBlueArmour()
                }
            }
            if (quest("goblin_diplomacy") == "gave_blue") {
                if (inventory.contains("goblin_mail")) {

                } else {
                    whereDoIGetBrownArmour()
                }
            }
            preferPeace()
            if (quest("goblin_diplomacy") == "unstarted") {
                pickAnArmour()
            }
            leaveYouToIt()
        }
    }

    fun ChoiceOption.iHaveOrangeArmour() = option<Neutral>("I have some orange armour here.") {
        cutscene()
    }

    suspend fun Player.cutscene() {
        open("fade_out")
        delay(4)
        val region = Region(9812)
        val cutscene = startCutscene("goblin_diplomacy", region)
        setInstanceLogout(Tile(2957, 3511))//todo last player loc
        //cutscene.onEnd {
       //     clearCamera()
       //     tele(2957, 3511)//todo last player loc
      // }

        val curtain = GameObjects.find(cutscene.tile(2441, 5431), "curtain_goblin_village")
        val grubfoot = NPCs.add("grubfoot_goblin_village", cutscene.tile(2442, 5431), Direction.EAST)
        val general_bentnoze = NPCs.add("general_bentnoze_cutscene", cutscene.tile(2446, 5432), Direction.WEST)
        val general_wartface = NPCs.add("general_wartface_cutscene", cutscene.tile(2446, 5431), Direction.WEST)
        tele(cutscene.tile(2445, 5433), clearInterfaces = false)
        moveCamera(cutscene.tile(2445, 5433), 320)
        turnCamera(cutscene.tile(2445, 5433), 320)
        delay(2)
        face(grubfoot)
        open("fade_in")
        npc<Neutral>("general_wartface_cutscene", "Grubfoot!", clickToContinue = true)
        grubfoot.walkTo(cutscene.tile(2444, 5431))
        npc<Quiz>("grubfoot_cutscene", "Yes General Wartface?", clickToContinue = true)
        npc<Neutral>("general_wartface_cutscene", "Put on this armour!")
        grubfoot.walkTo(cutscene.tile(2442, 5431))
        delay(4)
        grubfoot.mode = PauseMode
        curtain.anim("curtain_open")
        delay(1)
        grubfoot.walkOverDelay(cutscene.tile(2441, 5431))
        delay(1)
        grubfoot.mode = PauseMode
        curtain.anim("curtain_closing")
        delay(1)
        set("gobdip_grubfoot_vis", "orange")
        delay(1)
        curtain.anim("curtain_open")
        delay(1)
        grubfoot.walkOverDelay(cutscene.tile(2443, 5431))
        delay(1)
        grubfoot.mode = PauseMode
        curtain.anim("curtain_closing")
        delay(10)
        set("gobdip_grubfoot_vis", "brown")
        tele(2957, 3511)//remove once cutscene.onEnd { is fixed
        cutscene.end()
    }

}
