package rs.dusk.engine.client.update.task

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rs.dusk.engine.model.entity.Direction
import rs.dusk.engine.model.entity.index.Movement
import rs.dusk.engine.model.entity.index.player.Player
import rs.dusk.engine.model.entity.index.player.PlayerMoveType
import rs.dusk.engine.model.entity.index.player.Viewport
import rs.dusk.engine.model.entity.index.update.visual.player.movementType
import rs.dusk.engine.model.entity.index.update.visual.player.temporaryMoveType
import rs.dusk.engine.model.entity.list.entityListModule
import rs.dusk.engine.model.world.Tile
import rs.dusk.engine.path.TraversalStrategy
import rs.dusk.engine.script.KoinMock
import java.util.*

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since May 29, 2020
 */
internal class PlayerMovementTaskTest : KoinMock() {

    override val modules = listOf(entityListModule)

    lateinit var task: PlayerMovementTask
    lateinit var movement: Movement

    @BeforeEach
    fun setup() {
        movement = mockk(relaxed = true)
        task = PlayerMovementTask(mockk(relaxed = true))
    }

    @Test
    fun `Steps ignored if frozen`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        every { player.viewport } returns viewport
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        every { player.movement } returns movement
        every { movement.steps } returns steps
        every { movement.frozen } returns true
        every { viewport.loaded } returns true
        // When
        task.runAsync(player)
        // Then
        assertEquals(1, steps.size)
    }

    @Test
    fun `Steps ignored if viewport not loaded`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        every { player.viewport } returns viewport
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        every { player.movement } returns movement
        every { movement.steps } returns steps
        every { viewport.loaded } returns false
        // When
        task.runAsync(player)
        // Then
        assertEquals(1, steps.size)
    }

    @Test
    fun `Walk step`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        val traversal: TraversalStrategy = mockk(relaxed = true)
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        steps.add(Direction.NORTH)
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.MovementType")
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.TemporaryMoveType")
        every { player.movement } returns movement
        every { player.viewport } returns viewport
        every { movement.steps } returns steps
        every { movement.traversal } returns traversal
        every { viewport.loaded } returns true
        every { traversal.blocked(any(), any(), any(), Direction.NORTH) } returns false
        every { player.movementType = any() } just Runs
        every { player.temporaryMoveType = any() } just Runs
        // When
        task.runAsync(player)
        // Then
        verifyOrder {
            movement.walkStep = Direction.NORTH
            movement.delta = Direction.NORTH.delta
            player.movementType = PlayerMoveType.Walk
            player.temporaryMoveType = PlayerMoveType.Walk
        }
        assertEquals(1, steps.size)
    }

    @Test
    fun `Walk ignored if blocked`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        val traversal: TraversalStrategy = mockk(relaxed = true)
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        every { player.movement } returns movement
        every { player.viewport } returns viewport
        every { movement.steps } returns steps
        every { movement.traversal } returns traversal
        every { viewport.loaded } returns true
        every { traversal.blocked(any(), any(), any(), Direction.NORTH) } returns true
        every { player.movementType = any() } just Runs
        every { player.temporaryMoveType = any() } just Runs
        every { movement.running } returns false
        // When
        task.runAsync(player)
        // Then
        verify(exactly = 0) {
            movement.walkStep = Direction.NORTH
            movement.delta = Direction.NORTH.delta
        }
    }

    @Test
    fun `Run ignored if blocked`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        val traversal: TraversalStrategy = mockk(relaxed = true)
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        steps.add(Direction.NORTH)
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.MovementType")
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.TemporaryMoveType")
        every { player.movement } returns movement
        every { player.viewport } returns viewport
        every { movement.steps } returns steps
        every { movement.traversal } returns traversal
        every { viewport.loaded } returns true
        every { traversal.blocked(any(), any(), any(), Direction.NORTH) } returns true
        every { player.movementType = any() } just Runs
        every { player.temporaryMoveType = any() } just Runs
        every { movement.running } returns true
        every { movement.delta } returns Direction.NORTH.delta
        // When
        task.runAsync(player)
        // Then
        verify(exactly = 0) {
            movement.runStep = Direction.NORTH
            movement.delta = Tile(0, 2, 0)
            player.movementType = PlayerMoveType.Run
            player.temporaryMoveType = PlayerMoveType.Run
        }
    }

    @Test
    fun `Run step`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        val traversal: TraversalStrategy = mockk(relaxed = true)
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        steps.add(Direction.NORTH)
        steps.add(Direction.NORTH)
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.MovementType")
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.TemporaryMoveType")
        every { player.movement } returns movement
        every { player.viewport } returns viewport
        every { movement.steps } returns steps
        every { movement.traversal } returns traversal
        every { viewport.loaded } returns true
        every { traversal.blocked(any(), any(), any(), Direction.NORTH) } returns false
        every { player.movementType = any() } just Runs
        every { player.temporaryMoveType = any() } just Runs
        every { movement.running } returns true
        every { movement.delta } returns Direction.NORTH.delta
        every { movement.delta = any() } just Runs
        // When
        task.runAsync(player)
        // Then
        verifyOrder {
            movement.walkStep = Direction.NORTH
            movement.delta = Direction.NORTH.delta
            player.movementType = PlayerMoveType.Walk
            player.temporaryMoveType = PlayerMoveType.Walk
            movement.runStep = Direction.NORTH
            movement.delta = Tile(0, 2, 0)
            player.movementType = PlayerMoveType.Run
            player.temporaryMoveType = PlayerMoveType.Run
        }
        assertEquals(1, steps.size)
    }

    @Test
    fun `Run odd step walks`() {
        // Given
        val player: Player = mockk(relaxed = true)
        val viewport: Viewport = mockk(relaxed = true)
        val traversal: TraversalStrategy = mockk(relaxed = true)
        val steps = LinkedList<Direction>()
        steps.add(Direction.NORTH)
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.MovementType")
        mockkStatic("rs.dusk.engine.model.entity.index.update.visual.player.TemporaryMoveType")
        every { player.movement } returns movement
        every { player.viewport } returns viewport
        every { movement.steps } returns steps
        every { movement.traversal } returns traversal
        every { viewport.loaded } returns true
        every { traversal.blocked(any(), any(), any(), Direction.NORTH) } returns false
        every { player.movementType = any() } just Runs
        every { player.temporaryMoveType = any() } just Runs
        every { movement.running } returns true
        every { movement.delta } returns Direction.NORTH.delta
        every { movement.delta = any() } just Runs
        // When
        task.runAsync(player)
        // Then
        verifyOrder {
            movement.walkStep = Direction.NORTH
            movement.delta = Direction.NORTH.delta
            player.movementType = PlayerMoveType.Walk
            player.temporaryMoveType = PlayerMoveType.Walk
            player.movementType = PlayerMoveType.Walk
            player.temporaryMoveType = PlayerMoveType.Run
        }
        verify(exactly = 0) {
            movement.runStep = Direction.NORTH
            movement.delta = Tile(0, 2, 0)
        }
    }

}