package content.minigame.sorceress_garden

import world.gregs.voidps.engine.client.ui.open
import world.gregs.voidps.engine.data.definition.PatrolDefinitions
import world.gregs.voidps.engine.entity.character.mode.Patrol
import world.gregs.voidps.engine.entity.character.mode.interact.Interaction
import world.gregs.voidps.engine.entity.character.move.tele
import world.gregs.voidps.engine.entity.character.npc.npcOperate
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.npcSpawn
import world.gregs.voidps.engine.entity.obj.GameObject
import world.gregs.voidps.engine.entity.obj.objectOperate
import world.gregs.voidps.engine.inject
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.engine.queue.softQueue
import world.gregs.voidps.engine.suspend.SuspendableContext
import world.gregs.voidps.type.Tile
import content.skill.magic.jewellery.teleport
import content.entity.player.dialogue.Happy
import content.entity.player.dialogue.Neutral
import content.entity.player.dialogue.Quiz
import content.entity.player.dialogue.Uncertain
import content.entity.player.dialogue.type.choice
import content.entity.player.dialogue.type.item
import content.entity.player.dialogue.type.npc
import content.entity.player.dialogue.type.player
import content.entity.obj.door.Door.tile
import content.entity.sound.playSound

val patrols: PatrolDefinitions by inject()

objectOperate("Drink-from", "fountain_10") {
    player.anim("5796")
    player.softQueue("teleport", 6) {
        player.teleport(Tile(3321, 3141), "modern")
    }
}

objectOperate("Open", "sorceress_gate_winter") {
    enterGarden(target, player)
}

objectOperate("Open", "sorceress_gate_spring") {
    if (player.levels.get(Skill.Thieving) < 25) {
        item("highwayman_mask", 145, "You need a Thieving level of 25 to pick the lock of this gate.")
    } else {
        enterGarden(target, player)
    }
}

objectOperate("Open", "sorceress_gate_autumn") {
    if (player.levels.get(Skill.Thieving) < 45) {
        item("highwayman_mask", 145, "You need a Thieving level of 45 to pick the lock of this gate.")
    } else {
        enterGarden(target, player)
    }
}

objectOperate("Open", "sorceress_gate_summer") {
    if (player.levels.get(Skill.Thieving) < 65) {
        item("highwayman_mask", 145, "You need a Thieving level of 65 to pick the lock of this gate.")
    } else {
        enterGarden(target, player)
    }
}


fun enterGarden(target: GameObject, player: Player) {
    val direction = target.tile.delta(player.tile).toDirection()
    val vertical = target.rotation == 0 || target.rotation == 2
    val target = if (vertical && direction.isHorizontal() || !vertical && direction.isVertical()) {
        target.tile
    } else {
        tile(target, 1)
    }
    player.playSound("gate_open")
    player.tele(target)
}

npcSpawn("autumn_elemental_*") { npc ->
    val patrol = patrols.get(npc.id)
    npc.mode = Patrol(npc, patrol.waypoints)
}
npcSpawn("spring_elemental_*") { npc ->
    val patrol = patrols.get(npc.id)
    npc.mode = Patrol(npc, patrol.waypoints)
}
npcSpawn("summer_elemental_*") { npc ->
    val patrol = patrols.get(npc.id)
    npc.mode = Patrol(npc, patrol.waypoints)
}
npcSpawn("winter_elemental_*") { npc ->
    val patrol = patrols.get(npc.id)
    npc.mode = Patrol(npc, patrol.waypoints)
}


objectOperate("Pick-fruit", "sqirk_tree_summer", "sqirk_tree_spring", "sqirk_tree_autumn", "sqirk_tree_winter") {
    pickFruit(target.id.removePrefix("sqirk_tree_"))
}


suspend fun SuspendableContext<Player>.pickFruit(type: String) {
    if (player.inventory.isFull()) {
        //player<Neutral>("I cannot carry any more.")
        return
    }
    player.playSound("3407")
    player.animDelay("2280")
    player.inventory.add("${type}_sqirk")
    if (type == "summer") {
        player.experience.add(Skill.Thieving, 60.0)
    }
    if (type == "autumn") {
        player.experience.add(Skill.Thieving, 50.0)
    }
    if (type == "spring") {
        player.experience.add(Skill.Thieving, 40.0)
    }
    if (type == "winter") {
        player.experience.add(Skill.Thieving, 30.0)
    }
    delay(2)
    player.playSound("1930")
    player.gfx("188")
    player.open("fade_out")
    delay(2)
    player.tele(2911, 5470)
    player.open("fade_in")
}



npcOperate("Talk-to", "del_monty") {
    // npc<CatCalmTalk>("CatCalmTalk")
    //npc<CatCheerful>("CatCheerful")
    // npc<CatExplain>("CatExplain")
    //npc<CatShook>("CatShook")
    // npc<CatShouting>("CatShouting")
    // npc<CatSlowTalk>("CatSlowTalk")
    // npc<CatSlowTalkTwo>("CatSlowTalkTwo")
    //npc<CatSurprissed>("CatSurprissed")
    // npc<CatHappy>("CatHappy")
    // npc<CatPurring>("CatPurring")
    //npc<CatDisappointed>("CatDisappointed")
    // npc<CatDisappointedTwo>("CatDisappointedTwo")
    // npc<CatLaugh>("CatLaugh")
    //npc<CatSad>("CatSad")
    //npc<CatIntelligentCalm>("CatIntelligentCalm")


    npc<Neutral>("Hello, no-fur. What are you doing in my mistress's garden?")
    choice {
        option<Neutral>("Looking for sq'irks.") {
            npc<Neutral>("If it's sq'irks you're after then you've come to the right place. We've got four seasons' worth of them.")
            player<Neutral>("I've a couple of questions.")
            npc<Neutral>("I'd be happy to help a friend of the feline.")
            player<Neutral>("What do you mean?")
            npc<Neutral>("The Sphinx gave you that amulet, so she must hold you in high regard.")
            player<Neutral>(" If I remember correctly, it was the High Priest of Sophanem who gave it to me.")
            npc<Neutral>("He and the Sphinx are as thick as thieves, but regardless, I think you've a bit of a cat feel about you.")
            npc<Neutral>("Now what are these questions?")
            questions()
        }
        option<Neutral>("Talking to cats.") {
            npc<Neutral>("A noble and rewarding past-time.")
            choice {
                option<Quiz>("How did you get here?") {
                    getHere()
                }
                option<Neutral>("What are you doing here?") {
                    doingHere()
                }
                option<Quiz>("Who are you?") {
                    whoAreYou()
                }
            }
        }
        option<Neutral>("Nothing much.") {
            npc<Neutral>("Yawn! Nice talking to you then, no-fur.")
        }
    }
}

suspend fun Interaction<Player>.questions() {
    choice {
        option<Neutral>("What are the creatures inside the gardens?") {
            npc<Neutral>("Oh, you mean the gardeners?")
            player<Uncertain>("The strange, floaty creatures.")
            npc<Neutral>("Yes, the gardeners. Don't let them see you or they'll teleport you out here. They're very protective of their crops.")
            moreQuestions()
        }
        option<Neutral>("How do you get into the seasonal gardens?") {
            npc<Neutral>("I get in through gaps in the hedge. You are a little too big to squeeze through. I think you'll have to use the gates.")
            npc<Neutral>("I think the gates are locked, so you'll have to have good Thieving skills to get in.")
            moreQuestions()
        }
        option<Neutral>("How is it that the gardens are in different seasons?") {
            npc<Neutral>("How did you get here?")
            player<Uncertain>("Magic?")
            npc<Neutral>("Exactly. The Sorceress likes to have a good supply of in-season sq'irks and herbs at all times.")
            moreQuestions()
        }
        option<Neutral>("How do I get out of here?") {
            npc<Neutral>("Take a drink from the fountain.")
            moreQuestions()
        }
    }
}

suspend fun Interaction<Player>.moreQuestions() {
    choice {
        option<Neutral>("Thanks, I have another question though.") {
            questions()
        }
        option<Neutral>("Thanks for your help.") {
        }
    }
}

suspend fun Interaction<Player>.getHere() {
    npc<Neutral>("Every time I play with spiders in the Sorceress's house, her silly apprentice completely freaks out and teleports me here!")
    player<Neutral>("So you've been stuck here since?")
    npc<Neutral>("No, silly! I drink from the fountain whenever I want to leave.")
    anotherQuestion()
}

suspend fun Interaction<Player>.doingHere() {
    npc<Happy>("I get this strange urge for sq'irks. It's quite peculiar. I think I may be addicted.")
    player<Neutral>("I think I know someone else who may be in a similar position.")
    npc<Neutral>("Don't tell me. Osman, right?")
    player<Uncertain>("One of his spies, Selim. You know Osman?")
    npc<Neutral>("Oh, he used to come here all the time. Then one day, he just stopped.")
    anotherQuestion()
}

suspend fun Interaction<Player>.whoAreYou() {
    npc<Neutral>("Del-Monty the cat, at your service.")
    player<Neutral>("Are you a famous adventurer who was turned into a cat by a vindictive mage?")
    npc<Neutral>("No; as I said, I'm Del-Monty the cat, connoisseur of exotic fruits.")
    player<Neutral>("In that case, can you tell me anything about sq'irks?")
    npc<Neutral>("But of course! Their juice is an excellent source of energy for runners, and in the riper varieties they are known to heighten one's Thieving abilities.")
    anotherQuestion()
}

suspend fun Interaction<Player>.anotherQuestion() {
    choice {
        option<Neutral>("Thanks, I have another question though.") {
            choice {
                option<Quiz>("How did you get here?") {
                    getHere()
                }
                option<Neutral>("What are you doing here?") {
                    doingHere()
                }
                option<Quiz>("Who are you?") {
                    whoAreYou()
                }
            }
        }
        option<Neutral>("Thanks for your help.") {
        }
    }
}