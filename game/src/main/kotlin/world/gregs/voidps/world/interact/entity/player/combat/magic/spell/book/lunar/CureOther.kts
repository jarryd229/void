package world.gregs.voidps.world.interact.entity.player.combat.magic.spell.book.lunar

import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.client.ui.interact.itemOnPlayerApproach
import world.gregs.voidps.engine.client.variable.start
import world.gregs.voidps.engine.data.definition.SpellDefinitions
import world.gregs.voidps.engine.entity.character.player.name
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.inject
import world.gregs.voidps.world.interact.entity.player.combat.magic.spell.removeSpellItems
import world.gregs.voidps.world.interact.entity.player.toxin.curePoison
import world.gregs.voidps.world.interact.entity.player.toxin.poisoned

val definitions: SpellDefinitions by inject()

itemOnPlayerApproach(id = "lunar_spellbook", component = "cure_other") {
    approachRange(2)
    val spell = component
    if (!target.poisoned) {
        player.message("This player is not poisoned.")
        return@itemOnPlayerApproach
    }
    if (!player.removeSpellItems(spell)) {
        return@itemOnPlayerApproach
    }
    val definition = definitions.get(spell)
    player.start("movement_delay", 2)
    player.anim("lunar_cast")
    target.gfx(spell)
    player.experience.add(Skill.Magic, definition.experience)
    target.curePoison()
    target.message("You have been cured by ${player.name}.")
}
