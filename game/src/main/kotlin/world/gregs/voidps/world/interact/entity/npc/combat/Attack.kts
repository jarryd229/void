import world.gregs.voidps.engine.entity.character.move.retreat
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.entity.character.setAnimation
import world.gregs.voidps.engine.event.Priority
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.world.interact.entity.combat.CombatSwing
import world.gregs.voidps.world.interact.entity.combat.hit

on<CombatSwing>({ !swung() }, Priority.LOWEST) { npc: NPC ->
    npc.setAnimation(attackAnimation(npc))
    npc.hit(target, delay = 1)
    delay = npc.def["attack_speed", 4]
}

on<CombatSwing>({ it.tile.distanceTo(target) > it.def["attack_radius", 8] }, Priority.HIGHER) { npc: NPC ->
    delay = -1
    npc.retreat(target)
}

fun attackAnimation(npc: NPC): String {
    val race: String? = npc.def.getOrNull("race")
    if (race != null) {
        return "${race}_attack"
    }
    return npc.def["hit_anim"]
}