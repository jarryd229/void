package rs.dusk.network.rs.codec.game.encode

import rs.dusk.buffer.Modifier
import rs.dusk.buffer.write.writeByte
import rs.dusk.buffer.write.writeShort
import rs.dusk.core.network.codec.message.MessageEncoder
import rs.dusk.engine.entity.character.player.Player
import rs.dusk.network.rs.codec.game.GameOpcodes.FLOOR_ITEM_ADD

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since June 19, 2020
 */
class FloorItemAddMessageEncoder : MessageEncoder(FLOOR_ITEM_ADD) {

    /**
     * @param tile The tile offset from the chunk update send
     * @param id Item id
     * @param amount Item stack size
     */
    fun encode(
        player: Player,
        tile: Int,
        id: Int,
        amount: Int
    ) = player.send(5, flush = false) {
        writeByte(tile, type = Modifier.INVERSE)
        writeShort(id, type = Modifier.ADD)
        writeShort(amount)
    }
}