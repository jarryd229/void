package world.gregs.void.world.interact.entity.player.equip

import world.gregs.void.cache.definition.data.ItemDefinition
import world.gregs.void.engine.entity.character.contain.ContainerResult
import world.gregs.void.engine.entity.character.contain.equipment
import world.gregs.void.engine.entity.character.contain.inventory
import world.gregs.void.engine.entity.character.player.Player
import world.gregs.void.engine.entity.character.player.PlayerRegistered
import world.gregs.void.engine.entity.character.update.visual.player.emote
import world.gregs.void.engine.entity.character.update.visual.player.flagAppearance
import world.gregs.void.engine.entity.definition.ItemDefinitions
import world.gregs.void.engine.entity.item.EquipSlot
import world.gregs.void.engine.entity.item.EquipType
import world.gregs.void.engine.event.then
import world.gregs.void.engine.event.where
import world.gregs.void.network.codec.game.encode.message
import world.gregs.void.utility.inject

val itemDecoder: ItemDefinitions by inject()

ContainerAction where { container == "inventory" && (option == "Wield" || option == "Wear") } then {
    val details = itemDecoder.get(item)

    if (failedToRemoveOtherHand(player, details)) {
        player.message("Your inventory is full.")
        return@then
    }

    player.inventory.swap(slot, player.equipment, details["slot", EquipSlot.None].index)
    player.flagAppearance()
}

ContainerAction where { container == "worn_equipment" && option == "Remove" } then {
    if (!player.equipment.move(slot, player.inventory) && player.equipment.result == ContainerResult.Full) {
        player.message("You don't have enough inventory space.")
    }
}

PlayerRegistered then {
    player.equipment.listeners.add { list ->
        for ((index, _, _) in list) {
            if (index == EquipSlot.Weapon.index) {
                val weapon = player.equipment.getItem(EquipSlot.Weapon.index)
                val def = itemDecoder.get(weapon)
                val anim = def.getParam(644, 1426)
                player.emote = anim
            }
        }
    }
}

fun failedToRemoveOtherHand(player: Player, item: ItemDefinition): Boolean {
    return isHandSlot(item["slot", EquipSlot.None]) && hasTwoHandedWeapon(player, item) && failedToMoveToInventory(player, item)
}

fun failedToMoveToInventory(player: Player, item: ItemDefinition): Boolean {
    val otherSlot = getOtherHandSlot(item["slot", EquipSlot.None])
    if (player.equipment.isIndexFree(otherSlot.index)) {
        return false
    }
    return !movedEquipmentToInventory(player, otherSlot)
}

fun getOtherHandSlot(slot: EquipSlot) = if (slot == EquipSlot.Shield) EquipSlot.Weapon else EquipSlot.Shield

fun movedEquipmentToInventory(player: Player, oppositeSlot: EquipSlot): Boolean {
    return player.equipment.move(oppositeSlot.index, player.inventory)
}

fun isHandSlot(slot: EquipSlot) = slot == EquipSlot.Weapon || slot == EquipSlot.Shield

fun hasTwoHandedWeapon(player: Player, item: ItemDefinition) =
    isTwoHandedWeapon(item) || holdingTwoHandedWeapon(player)

fun isTwoHandedWeapon(item: ItemDefinition) = item["slot", EquipSlot.None] == EquipSlot.Weapon && item["type", EquipType.None] == EquipType.TwoHanded

fun holdingTwoHandedWeapon(player: Player): Boolean {
    val weapon = player.equipment.getItem(EquipSlot.Weapon.index)
    return itemDecoder.get(weapon)["type", EquipType.None] == EquipType.TwoHanded
}