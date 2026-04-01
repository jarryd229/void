package content.area.kandarin.feldip_hills

import content.entity.player.dialogue.*
import content.entity.player.dialogue.type.choice
import content.entity.player.dialogue.type.item
import content.entity.player.dialogue.type.npc
import content.entity.player.dialogue.type.player
import content.entity.player.dialogue.type.statement
import content.quest.quest
import content.quest.refreshQuestJournal
import world.gregs.voidps.engine.Script
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.inv.carriesItem
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.engine.inv.remove

class Rants : Script {

    init {
        npcOperate("Talk-to", "rantz_feldip_hills_2_2") {
            when (quest("big_chompy_bird_hunting")) {
                "unstarted" -> {
                    npc<Neutral>("Hey you creature! Make me some stabbers! I wanna hunt da chompy?")
                    player<Confused>("Stabbers?")
                    npc<Neutral>("For da stabbie chucker, I's wanna hunt da chompy! Da chompy is der bestest yummies for Rantz, Fycie and Bugs! Creature knows what Rantz wants... flyin' to stabbie da chompy!")
                    item("ogre_bow", 1000, "The ogre shows you a huge but crude bow and then starts to nod energetically in an effort to help you understand.")
                    player<Quiz>("I think I understand. You want me to make some arrows for you?")
                    npc<Neutral>("Yeah, is what Rantz sayed, make da stabbers for da stabby chucker!")

                    if (levels.get(Skill.Fletching) < 5 || levels.get(Skill.Cooking) < 30  || levels.get(Skill.Ranged) < 30) {
                        statement("Before starting this quest, be aware that one or more of your skill levels are lower than what is required to fully complete it.")
                    }
                    choice("Start the Big Chompy Bird Hunting quest?") {
                        option("Yes.") {
                            player<Neutral>("OK, I'll make you some 'stabbers'.")
                            npc<Neutral>("Good you creature, you need sticksies from achey tree and stabbies from dog bones.")
                            set("big_chompy_bird_hunting", "started")
                            refreshQuestJournal()
                        }
                        option("No.") {
                            player<Neutral>("Er, make you're own 'stabbers'!")
                            npc<Angry>("When I make 'stabbers', I pretend you chompy and practice on you!")
                        }
                    }
                }
                "started" -> started()
                "gave_arrows" -> {
                    npc<Quiz>("Hey you creature, you still here?")
                    npc<Quiz>("Da chompy still not coming! We need da fatsy toady to get da chompy, do you got it? Do you got da fatsy toady? Then we can sneaky, sneaky stick da chompy.")


                    player<Angry>("No I haven't got the 'fatsy toady' yet!")
                    npc<Sad>("Dat's a pidy... but maybe Rantz can help da creature?")
                    gaveArrowsMenu()
                }
                else -> completed()
            }
        }
    }

    suspend fun Player.started() {
        npc<Neutral>("Hey you creature... Have you made me da stabbers? I wanna stick da chompy?")
        if (carriesItem("ogre_arrow", 6)) {
            player<Angry>("Well, yes actually, as you asked so nicely. Here you go! Here's your 'stabbers'.")
            inventory.remove("ogre_arrow", 6)
            set("big_chompy_bird_hunting", "gave_arrows")
            item("ogre_arrow_2",  1000, "Rantz takes six ogre arrows off you.")
            npc<Happy>("Ahh, der creature has dem... goodly, goodly. Now us can stick der chompy bird...")
            npc<Sad>("But da chompy not coming without da fatsy toadies... Godda get der fatsy toadies to get da chompys. Den we put it over de're and sneaky, sneaky stick da chompy.")
            gaveArrowsMenu()
        } else {
            player<Neutral>("Er not exactly?")
            npc<Angry>("You do stabbers quick quick! Or Rantz make stabbers for Rantz and then practice for chompy sticking on creature!")
            choice {
                option<Neutral>("How do I make the 'stabbers' again?") {
                    npc<Neutral>("Grrr creature... you's no good stabber maker! You's make da stabbie bit from da dog bones, and get da sticksies from da Achey tree... simple see? Oh and da flufsies from da flappers as well!")
                    player<Quiz>("Oh, so I need logs from the achey tree, bones from a canine... and feathers?")
                    npc<Happy>("Is just what Rantz sayed! <col=0000ff>~ The hulking ogre nods excitedly. ~")
                }
                option<Neutral>("Ok, I'll make the 'stabbers' for you.") {
                    npc<Neutral>("Good creature, quickly, hurry bring da stabbers!")
                }
            }
        }
    }

    suspend fun Player.gaveArrowsMenu() {
        choice {
            option<Neutral>("How do we make the chompys come?") {
                npc<Neutral>("Chompys love da fatsy toadies. Toadies get big on der swamp gas and der chompys are licking der lips for em as me is licking lips for da chompy. Da chompys don't like da smaller toadies from nearby swampy.")

                npc<Happy>("Dey's fussie eaters just like Rantz. Fycie an' Bugs play with toadies and blower dey's all times making fatsy toadies.")
                gaveArrowsMenu()
            }
            option<Neutral>("What are 'fatsy toadies'?") {
                npc<Neutral>("Fatsy toadies are da chompy burds bestest yumms. But da toadies here are too small for da chompy. You've godda make da toadies big and round!")
                gaveArrowsMenu()
            }
            option<Neutral>("Where do we put the 'fatsy toadies'?") {
                npc<Angry>("Over der!")
                statement("The ogre points to a small clearing to da south.")
                npc<Angry>("Ok creature? You got dat? Over dere by der no tree's place.")
                gaveArrowsMenu()
            }
            option<Neutral>("What do you mean 'sneaky..sneaky, stick the chompy?'") {
                npc<Quiz>("Duh! You creature is a bit stoopid yes? Us needs to sneaky, sneaky and stick da chompy! Den we can eat da chompy!")
                gaveArrowsMenu()
            }
            option<Neutral>("Ok, thanks.") {
            }
        }
    }

    suspend fun Player.completed() {
    }

}
