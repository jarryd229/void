package content.area.misthalin.varrock

import content.entity.combat.hit.damage
import content.entity.npc.shop.buy
import content.entity.player.dialogue.Angry
import content.entity.player.dialogue.Quiz
import content.entity.player.dialogue.Sad
import content.entity.player.dialogue.Talk
import content.entity.player.dialogue.type.choice
import content.entity.player.dialogue.type.npc
import content.entity.player.dialogue.type.player
import content.quest.miniquest.alfred_grimhands_barcrawl.barCrawlDrink
import content.quest.miniquest.alfred_grimhands_barcrawl.barCrawlFilter
import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.client.ui.interact.itemOnNPCApproach
import world.gregs.voidps.engine.data.Settings
import world.gregs.voidps.engine.entity.character.mode.interact.TargetInteraction
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.entity.character.npc.npcApproach
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.chat.noInterest
import world.gregs.voidps.engine.entity.character.player.skill.Skill

npcApproach("Talk-to", "bartender_blue_moon_inn") {
    approachRange(4)
    npc<Quiz>("What can I do yer for?")
    choice {
        option<Talk>("A glass of your finest ale please.") {
            npc<Talk>("No problemo. That'll be 2 coins.")
            if (buy("beer", 2)) {
                player.message("You buy a pint of beer.")
            }
        }
        option<Quiz>("Can you recommend where an adventurer might make his fortune?") {
            npc<Angry>("Ooh I don't know if I should be giving away information, makes the game too easy.")
            choice {
                option<Talk>("Oh ah well...")
                option<Quiz>("Game? What are you talking about?") {
                    npc<Angry>("This world around us... is an online game... called ${Settings["server.name"]}.")
                    player<Quiz>("Nope, still don't understand what you are talking about. What does 'online' mean?")
                    npc<Angry>("It's a sort of connection between magic boxes across the world, big boxes on people's desktops and little ones people can carry. They can talk to each other to play games.")
                    player<Angry>("I give up. You're obviously completely mad!")
                }
                option<Quiz>("Just a small clue?") {
                    npc<Angry>("Go and talk to the bartender at the Jolly Boar Inn, he doesn't seem to mind giving away clues.")
                }
                option<Quiz>("Do you know where I can get some good equipment?") {
                    npc<Talk>("Well, there's the sword shop across the road, or there's also all sorts of shops up around the market.")
                }
            }
        }
        option("I'm doing Alfred Grimhand's barcrawl.", filter = barCrawlFilter) {
            barCrawl()
        }
    }
}

itemOnNPCApproach("barcrawl_card", "bartender_blue_moon_inn") {
    if (player.containsVarbit("barcrawl_signatures", "uncle_humphreys_gutrot")) {
        player.noInterest() // TODO proper message
        return@itemOnNPCApproach
    }
    barCrawl()
}

suspend fun TargetInteraction<Player, NPC>.barCrawl() = barCrawlDrink(
    start = {
        npc<Sad>("Oh no not another of you guys. These barbarian barcrawls cause too much damage to my bar.")
        npc<Talk>("You're going to have to pay 50 gold for the Uncle Humphrey's Gutrot.")
    },
    effects = {
        player.levels.drain(Skill.Attack, 6)
        player.levels.drain(Skill.Defence, 6)
        player.levels.drain(Skill.Strength, 6)
        player.levels.drain(Skill.Smithing, 6)
        player.damage(0)
        player.say("Blearrgh!")
    },
)
