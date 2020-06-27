import rs.dusk.engine.event.priority
import rs.dusk.engine.event.then
import rs.dusk.engine.model.entity.Direction
import rs.dusk.engine.model.entity.Direction.Companion.cardinal
import rs.dusk.engine.model.entity.Direction.Companion.ordinal
import rs.dusk.engine.model.entity.Registered
import rs.dusk.engine.model.entity.Unregistered
import rs.dusk.engine.model.entity.obj.Location
import rs.dusk.engine.model.world.map.collision.*
import rs.dusk.engine.model.world.map.collision.CollisionFlag.IGNORED
import rs.dusk.engine.model.world.map.collision.CollisionFlag.LAND
import rs.dusk.engine.model.world.map.collision.CollisionFlag.NORTH_OR_EAST
import rs.dusk.engine.model.world.map.collision.CollisionFlag.NORTH_OR_WEST
import rs.dusk.engine.model.world.map.collision.CollisionFlag.SKY
import rs.dusk.engine.model.world.map.collision.CollisionFlag.SOUTH_OR_EAST
import rs.dusk.engine.model.world.map.collision.CollisionFlag.SOUTH_OR_WEST
import rs.dusk.utility.inject

val collisions: Collisions by inject()

Registered priority 9 where { entity is Location } then {
    modifyCollision(entity as Location, ADD_MASK)
}

Unregistered priority 9 where { entity is Location } then {
    modifyCollision(entity as Location, REMOVE_MASK)
}

fun modifyCollision(location: Location, changeType: Int) {
    if (location.def.solid == 0) {
        return
    }

    when (location.type) {
        in 0..3 -> modifyWall(location, changeType)
        in 9..21 -> modifyObject(location, changeType)
        22 -> {
            if (location.def.solid == 1) {
                modifyMask(location.tile.x, location.tile.y, location.tile.plane, CollisionFlag.FLOOR_DECO, changeType)
            }
        }
    }
}

fun modifyObject(location: Location, changeType: Int) {
    var mask = LAND

    if (location.def.blocksSky) {//solid
        mask = mask or SKY
    }

    if (!location.def.ignoreOnRoute) {//not alt
        mask = mask or IGNORED
    }

    var width = location.size.width
    var height = location.size.height

    if (location.rotation and 0x1 == 1) {
        width = location.size.height
        height = location.size.width
    }

    for (offsetX in 0 until width) {
        for (offsetY in 0 until height) {
            modifyMask(location.tile.x + offsetX, location.tile.y + offsetY, location.tile.plane, mask, changeType)
        }
    }
}


fun modifyWall(location: Location, changeType: Int) {
    modifyWall(location, 0, changeType)
    if (location.def.blocksSky) {
        modifyWall(location, 1, changeType)
    }
    if (!location.def.ignoreOnRoute) {
        modifyWall(location, 2, changeType)
    }
}

/**
 * Wall types:
 * 0 - ║ External wall (vertical or horizontal)
 * 1 - ╔ External corner (flat/missing)
 * 2 - ╝ Internal corner
 * 3 - ╔ External corner (regular)
 */
fun modifyWall(location: Location, motion: Int, changeType: Int) {
    val rotation = location.rotation
    val type = location.type
    var tile = location.tile

    // Internal corners
    if (type == 2) {
        // Mask both cardinal directions
        val or = when (ordinal[rotation and 0x3]) {
            Direction.NORTH_WEST -> NORTH_OR_WEST
            Direction.NORTH_EAST -> NORTH_OR_EAST
            Direction.SOUTH_EAST -> SOUTH_OR_EAST
            Direction.SOUTH_WEST -> SOUTH_OR_WEST
            else -> 0
        }
        modifyMask(location.tile.x, location.tile.y, location.tile.plane, applyMotion(or, motion), changeType)
        tile = tile.add(cardinal[(rotation + 3) and 0x3].delta)
    }

    // Mask one wall side
    var direction = when (type) {
        0 -> cardinal[(rotation + 3) and 0x3]
        2 -> cardinal[(rotation + 1) and 0x3]
        else -> ordinal[rotation and 0x3]
    }
    modifyMask(tile.x, tile.y, tile.plane, direction.flag(motion), changeType)

    // Mask other wall side
    tile = if (type == 2) {
        location.tile.add(cardinal[rotation and 0x3].delta)
    } else {
        location.tile.add(direction.delta)
    }
    direction = when (type) {
        2 -> cardinal[(rotation + 2) and 0x3]
        else -> direction.inverse()
    }
    modifyMask(tile.x, tile.y, tile.plane, direction.flag(motion), changeType)
}

val ADD_MASK = 0
val REMOVE_MASK = 1
val SET_MASK = 2

fun modifyMask(x: Int, y: Int, plane: Int, mask: Int, changeType: Any) {
    when (changeType) {
        ADD_MASK -> collisions.add(x, y, plane, mask)
        REMOVE_MASK -> collisions.remove(x, y, plane, mask)
        SET_MASK -> collisions[x, y, plane] = mask
    }
}

fun applyMotion(mask: Int, motion: Int): Int {
    return when (motion) {
        1 -> mask shl 9
        2 -> mask shl 22
        else -> mask
    }
}

fun Direction.flag(motion: Int) = applyMotion(flag(), motion)