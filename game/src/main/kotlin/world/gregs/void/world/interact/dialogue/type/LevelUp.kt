package world.gregs.void.world.interact.dialogue.type

import world.gregs.void.engine.client.ui.dialogue.DialogueContext
import world.gregs.void.engine.client.ui.open
import world.gregs.void.engine.client.variable.setVar

private const val LEVEL_UP_INTERFACE_NAME = "level_up_dialog"

suspend fun DialogueContext.levelUp(text: String, skill: Int) {
    val lines = text.trimIndent().lines()
    if (player.open(LEVEL_UP_INTERFACE_NAME)) {
        for ((index, line) in lines.withIndex()) {
            player.interfaces.sendText(LEVEL_UP_INTERFACE_NAME, "line${index + 1}", line)
        }
        player.setVar("level_up_icon", skill)
        return await("level")
    }
}