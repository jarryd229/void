package world.gregs.voidps.network.visual.encode

import world.gregs.voidps.buffer.write.Writer
import world.gregs.voidps.network.visual.VisualEncoder
import world.gregs.voidps.network.visual.Visuals

class WatchEncoder(mask: Int) : VisualEncoder<Visuals>(mask) {

    override fun encode(writer: Writer, visuals: Visuals) {
        writer.writeShort(visuals.watch.index)
    }

}