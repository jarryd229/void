package content.entity.npc.combat.melee

import content.entity.combat.npcCombatPrepare
import content.entity.effect.toxin.poison
import world.gregs.voidps.type.random

npcCombatPrepare("poison_scorpion") { npc ->
    if (random.nextInt(2) == 0) { // Unknown rate
        val poison = npc.def["poison", 0]
        npc.poison(target, poison)
    }
}

npcCombatPrepare("cave_crawler*") { npc ->
    if (random.nextInt(2) == 0) { // Unknown rate
        val poison = npc.def["poison", 0]
        npc.poison(target, poison)
    }
}

npcCombatPrepare("poison_spider") { npc ->
    if (random.nextInt(2) == 0) { // Unknown rate
        val poison = npc.def["poison", 0]
        npc.poison(target, poison)
    }
}

npcCombatPrepare("kalphite_guardian") { npc ->
    if (random.nextInt(2) == 0) { // Unknown rate
        val poison = npc.def["poison", 0]
        npc.poison(target, poison)
    }
}

npcCombatPrepare("cave_slime") { npc ->
    val poison = npc.def["poison", 0]
    npc.poison(target, poison)
}
