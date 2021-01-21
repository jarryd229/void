package world.gregs.void.network.codec.game.decode

import io.netty.channel.ChannelHandlerContext
import world.gregs.void.buffer.read.Reader
import world.gregs.void.network.codec.Decoder

class PingReplyDecoder : Decoder(8) {

    override fun decode(context: ChannelHandlerContext, packet: Reader) {
        handler?.pingReply(
            context,
            packet.readInt(),
            packet.readInt()
        )
    }

}