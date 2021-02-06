package world.gregs.voidps.world.interact.entity.obj

import world.gregs.voidps.engine.entity.obj.GameObject
import world.gregs.voidps.engine.event.Event
import world.gregs.voidps.engine.event.EventBus
import world.gregs.voidps.engine.event.EventCompanion
import world.gregs.voidps.utility.get

/**
 * Removes an existing map [gameObject].
 * The removal can be permanent if [ticks] is -1 or temporary
 * [owner] is also optional to allow for an object to removed just for one player.
 */
data class RemoveObject(
    val gameObject: GameObject,
    val ticks: Int,
    val owner: String? = null
) : Event<Unit>() {
    companion object : EventCompanion<RemoveObject>
}

fun GameObject.remove(ticks: Int = -1, owner: String? = null) {
    get<EventBus>().emit(RemoveObject(this, ticks, owner))
}

fun removeObject(
    original: GameObject,
    ticks: Int = -1,
    owner: String? = null
) = get<EventBus>().emit(RemoveObject(original, ticks, owner))