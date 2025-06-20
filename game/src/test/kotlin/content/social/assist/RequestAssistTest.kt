package content.social.assist

import WorldTest
import interfaceOption
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import playerOption
import walk
import world.gregs.voidps.engine.client.ui.hasOpen
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.req.hasRequest
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import kotlin.test.assertFalse

internal class RequestAssistTest : WorldTest() {

    @Test
    fun `Grant exp to assistant`() {
        val (assistant, receiver) = setupAssist()
        assistant.experience.set(Skill.Magic, 15000000.0)
        assistant.levels.set(Skill.Magic, 99)
        receiver.experience.set(Skill.Magic, 10000000.0)
        receiver.levels.set(Skill.Magic, 96)
        receiver.inventory.add("fire_rune")
        receiver.inventory.add("air_rune", 3)
        receiver.inventory.add("law_rune")

        assistant.interfaceOption("assist_xp", "magic", "Toggle Skill On / Off")
        tick()
        receiver.interfaceOption("modern_spellbook", "varrock_teleport", "Cast")
        tickIf { receiver.tile == emptyTile.addY(1) }

        assertTrue(assistant.experience.get(Skill.Magic) > 15000000.0)
        assertEquals(10000000.0, receiver.experience.get(Skill.Magic))
    }

    @Test
    fun `Assistance stops when more than 20 tiles away`() {
        val (assistant, receiver) = setupAssist()

        receiver.walk(emptyTile.addY(22))
        tickIf { receiver.tile != emptyTile.addY(22) }
        tick()

        assertFalse(assistant.hasOpen("assist_xp"))
    }

    @Test
    fun `Cancel assistance by changing filter`() {
        val (assistant, receiver) = setupAssist()

        receiver.interfaceOption("filter_buttons", "assist", "Off Assist")

        assertFalse(assistant.hasOpen("assist_xp"))
    }

    @Test
    fun `Can't assist with filter on`() {
        val assistant = createPlayer(emptyTile, "assistant")
        val receiver = createPlayer(emptyTile.addY(1), "receiver")
        assistant["assist_filter"] = "off"

        receiver.playerOption(assistant, "Req Assist")
        assertFalse(receiver.hasRequest(assistant, "assist"))
    }

    @Test
    fun `Can't assist with accept aid off`() {
        val assistant = createPlayer(emptyTile, "assistant")
        val receiver = createPlayer(emptyTile.addY(1), "receiver")
        assistant["accept_aid"] = false

        receiver.playerOption(assistant, "Req Assist")
        assertFalse(receiver.hasRequest(assistant, "assist"))
    }

    private fun setupAssist(): Pair<Player, Player> {
        val assistant = createPlayer(emptyTile, "assistant")
        val receiver = createPlayer(emptyTile.addY(1), "receiver")
        receiver.playerOption(assistant, "Req Assist")
        tick()
        assistant.playerOption(receiver, "Req Assist")
        tick()
        assertTrue(assistant.hasOpen("assist_xp"))
        return Pair(assistant, receiver)
    }
}
