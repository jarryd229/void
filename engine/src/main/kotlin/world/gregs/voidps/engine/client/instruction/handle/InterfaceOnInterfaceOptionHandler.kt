package world.gregs.voidps.engine.client.instruction.handle

import world.gregs.voidps.engine.client.instruction.InstructionHandler
import world.gregs.voidps.engine.client.instruction.InterfaceHandler.getInterfaceItem
import world.gregs.voidps.engine.client.ui.interact.InterfaceOnInterface
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.sync
import world.gregs.voidps.network.instruct.InteractInterfaceItem

/**
 * @author Jacob Rhiel <jacob.rhiel@gmail.com>
 * @created Jun 20, 2021
 */
class InterfaceOnInterfaceOptionHandler : InstructionHandler<InteractInterfaceItem>() {

    override fun validate(player: Player, instruction: InteractInterfaceItem) = sync {
        val (fromItemId, toItemId, fromSlot, toSlot, fromInterfaceId, fromComponentId, toInterfaceId, toComponentId) = instruction

        val (fromId, fromComponent, fromItem, fromContainer) = getInterfaceItem(player, fromInterfaceId, fromComponentId, fromItemId, fromSlot) ?: return@sync
        val (toId, toComponent, toItem, toContainer) = getInterfaceItem(player, toInterfaceId, toComponentId, toItemId, toSlot) ?: return@sync

        player.events.emit(
            InterfaceOnInterface(
                fromItem,
                toItem,
                fromSlot,
                toSlot,
                fromId,
                fromComponent,
                toId,
                toComponent,
                fromContainer,
                toContainer
            )
        )
    }

}