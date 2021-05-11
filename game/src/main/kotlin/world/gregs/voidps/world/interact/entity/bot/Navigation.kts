import world.gregs.voidps.ai.toDouble
import world.gregs.voidps.engine.action.ActionType
import world.gregs.voidps.engine.entity.Registered
import world.gregs.voidps.engine.entity.character.get
import world.gregs.voidps.engine.entity.character.getOrNull
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.set
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.engine.map.area.MapArea
import world.gregs.voidps.network.instruct.Walk
import world.gregs.voidps.world.interact.entity.bot.*

val walkToTarget = SimpleBotOption(
    "continue walking to target",
    targets = { empty },
    considerations = listOf(
        { (bot.action.type == ActionType.None || bot.action.type == ActionType.Movement).toDouble() },
        { (bot["navigating", false]).toDouble() },
        { (bot.steps?.isNotEmpty() == true || bot.movement.waypoints.isNotEmpty()).toDouble() }
    ),
    weight = 0.9,
    action = {
        if (isNearingStepCompletion(bot)) {
            val steps = bot.steps ?: return@SimpleBotOption
            if (steps.isEmpty()) {
                for (next in bot.movement.waypoints) {
                    steps.addAll(next.steps)
                }
                bot.movement.waypoints.clear()
            }
            if (steps.isNotEmpty()) {
                val step = steps.poll()
                bot.step = step
                bot.instructions.tryEmit(step)
                if (steps.isEmpty()) {
                    bot.action.completion = {
                        bot["navigating"] = false
                        val target: MapArea? = bot.getOrNull("targetArea")
                        if (target != null) {
                            bot["area"] = target
                        }
                    }
                }
            }
        }
    }
)

fun isNearingStepCompletion(bot: Player): Boolean {
    if (bot.step is Walk) {
        val walk = bot.step as Walk
        if (bot.tile.within(walk.x, walk.y, 1)) {
            return true
        }
        val next = bot.steps?.peek() as? Walk
        if (next != null && bot.tile.within(next.x, next.y, 20)) {
            return true
        }
    }
    return bot.action.type == ActionType.None
}

on<Registered>({ it.isBot }) { player: Player ->
    player.botOptions.add(walkToTarget)
}