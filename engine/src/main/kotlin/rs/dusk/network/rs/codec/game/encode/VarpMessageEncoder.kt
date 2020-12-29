package rs.dusk.network.rs.codec.game.encode

import rs.dusk.buffer.Modifier
import rs.dusk.buffer.write.writeByte
import rs.dusk.core.network.codec.message.MessageEncoder
import rs.dusk.engine.entity.character.player.Player
import rs.dusk.network.rs.codec.game.GameOpcodes.CLIENT_VARP
import rs.dusk.utility.get

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since July 04, 2020
 */
class VarpMessageEncoder : MessageEncoder(CLIENT_VARP) {

    /**
     * A variable player config; also known as "Config", known in the client as "clientvarp"
     * @param id The config id
     * @param value The value to pass to the config
     */
    fun encode(
        player: Player,
        id: Int,
        value: Int
    ) = player.send(3) {
        writeShort(id)
        writeByte(value, Modifier.ADD)
    }
}

fun Player.sendVarp(id: Int, value: Int) {
    if(value in Byte.MIN_VALUE..Byte.MAX_VALUE) {
        get<VarpMessageEncoder>().encode(this, id, value)
    } else {
        get<VarpLargeMessageEncoder>().encode(this, id, value)
    }
}