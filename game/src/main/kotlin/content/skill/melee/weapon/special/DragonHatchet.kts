package content.skill.melee.weapon.special

import content.entity.player.combat.special.specialAttackDamage
import world.gregs.voidps.engine.entity.character.player.skill.Skill

specialAttackDamage("clobber") {
    val drain = damage / 100
    if (drain > 0) {
        target.levels.drain(Skill.Defence, drain)
        target.levels.drain(Skill.Magic, drain)
    }
}
