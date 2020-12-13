package day12.part2

import day12.Coordinate
import day12.Heading
import day12.Instruction

class ShipV2(var location: Location = Location(), var waypoint: Waypoint = Waypoint()) {

    fun follow(instruction: Instruction): ShipV2 {
        val value = instruction.second

        when (instruction.first) {
            'N' -> moveWaypoint(Pair(Heading.NORTH, value))
            'S' -> moveWaypoint(Pair(Heading.SOUTH, value))
            'E' -> moveWaypoint(Pair(Heading.EAST, value))
            'W' -> moveWaypoint(Pair(Heading.WEST, value))
            'F' -> moveShipToFollowWaypoint(value)
            else -> rotateWaypoint(instruction.first, value)
        }

        return this
    }

    private fun moveWaypoint(direction: Pair<Heading, Int>) {
        val heading = direction.first
        val value = direction.second
        val coordinateAxis = heading.axisToMovementDirection.first
        val axisMovementDirection = heading.axisToMovementDirection.second

        when (coordinateAxis) {
            Coordinate.X_EAST_WEST -> waypoint.xEastWest += value * axisMovementDirection
            Coordinate.Y_NORTH_SOUTH -> waypoint.yNorthSouth += value * axisMovementDirection
        }
    }

    private fun moveShipToFollowWaypoint(times: Int) {
        repeat(times) {
            location.xEastWest += waypoint.xEastWest
            location.yNorthSouth += waypoint.yNorthSouth
        }
    }

    private fun rotateWaypoint(direction: Char, degrees: Int) {
        val isLeftBy90orRightBy270 = (direction == 'L' && degrees == 90) || (direction == 'R' && degrees == 270)
        val isRightBy90orLeftBy270 = (direction == 'R' && degrees == 90) || (direction == 'L' && degrees == 270)

        val is180 = degrees == 180

        val currentX = waypoint.xEastWest
        val currentY = waypoint.yNorthSouth

        when {
            is180 -> {
                waypoint.xEastWest *= -1
                waypoint.yNorthSouth *= -1
            }
            isLeftBy90orRightBy270 -> {
                waypoint.xEastWest = currentY
                waypoint.yNorthSouth = currentX * -1
            }
            isRightBy90orLeftBy270 -> {
                waypoint.xEastWest = currentY * -1
                waypoint.yNorthSouth = currentX
            }
        }
    }
}
