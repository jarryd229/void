package content.skill.melee.armour

import content.entity.combat.hit.combatDamage
import content.entity.combat.hit.directHit
import content.entity.player.inv.inventoryItem
import world.gregs.voidps.engine.client.message
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.entity.character.player.equip.equipped
import world.gregs.voidps.engine.inv.charges
import world.gregs.voidps.engine.inv.discharge
import world.gregs.voidps.engine.inv.equipment
import world.gregs.voidps.network.login.protocol.visual.update.player.EquipSlot

combatDamage { player ->
    if (source == player || type == "deflect" || type == "poison" || type == "disease" || type == "healed" || damage < 1) {
        return@combatDamage
    }
    if (player.equipped(EquipSlot.Ring).id != "ring_of_recoil") {
        return@combatDamage
    }
    if (source is NPC && source.def["immune_deflect", false]) {
        return@combatDamage
    }
    val charges = player.equipment.charges(player, EquipSlot.Ring.index)
    val deflect = (10 + (damage / 10)).coerceAtMost(charges)
    if (player.equipment.discharge(player, EquipSlot.Ring.index, deflect)) {
        source.directHit(deflect, "deflect", source = player)
    }
}

inventoryItem("Check", "ring_of_recoil", "worn_equipment") {
    val charges = player.equipment.charges(player, EquipSlot.Ring.index)
    player.message("You can inflict $charges more points of damage before a ring will shatter.")
}
