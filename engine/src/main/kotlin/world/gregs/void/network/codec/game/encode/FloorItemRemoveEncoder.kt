package world.gregs.void.network.codec.game.encode

import world.gregs.void.engine.entity.character.player.Player
import world.gregs.void.network.codec.Encoder
import world.gregs.void.network.codec.game.GameOpcodes.FLOOR_ITEM_REMOVE

/**
 * @author GregHib <greg@gregs.world>
 * @since June 19, 2020
 */
class FloorItemRemoveEncoder : Encoder(FLOOR_ITEM_REMOVE) {

    /**
     * @param tile The tile offset from the chunk update send
     * @param id Item id
     */
    fun encode(
        player: Player,
        tile: Int,
        id: Int
    ) = player.send(3, flush = false) {
        writeShort(id)
        writeByte(tile)
    }
}