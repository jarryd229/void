package rs.dusk.network.codec.game.decode

import io.netty.channel.ChannelHandlerContext
import rs.dusk.buffer.Endian
import rs.dusk.buffer.Modifier
import rs.dusk.buffer.read.Reader
import rs.dusk.network.codec.Decoder

class APCoordinateDecoder : Decoder(12) {

    override fun decode(context: ChannelHandlerContext, packet: Reader) {
        handler?.apCoordinate(
            context,
            packet.readShort(Modifier.ADD),
            packet.readShort(order = Endian.LITTLE),
            packet.readInt(order = Endian.MIDDLE),
            packet.readShort(Modifier.ADD),
            packet.readShort()
        )
    }
}