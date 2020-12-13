package day12.part1

import day12.Coordinate.X_EAST_WEST
import day12.Coordinate.Y_NORTH_SOUTH
import day12.Heading
import day12.Instruction

class ShipV1(var location: Location = Location()) {

    fun follow(instruction: Instruction): ShipV1 {
        val value = instruction.second

        when (instruction.first) {
            'N' -> followDirection(Pair(Heading.NORTH, value))
            'S' -> followDirection(Pair(Heading.SOUTH, value))
            'E' -> followDirection(Pair(Heading.EAST, value))
            'W' -> followDirection(Pair(Heading.WEST, value))
            'F' -> followDirection(Pair(resolveHeadingRelativeToDirection(instruction, location.heading), value))
            else -> followDirection(Pair(resolveHeadingRelativeToDirection(instruction, location.heading), 0))
        }

        return this
    }

    private fun followDirection(direction: Pair<Heading, Int>) {
        val heading = direction.first
        val value = direction.second
        val coordinateAxis = heading.axisToMovementDirection.first
        val axisMovementDirection = heading.axisToMovementDirection.second

        if (value == 0) {
            this.location.heading = heading
        } else {
            when (coordinateAxis) {
                X_EAST_WEST -> location.xEastWest += value * axisMovementDirection
                Y_NORTH_SOUTH -> location.yNorthSouth += value * axisMovementDirection
            }
        }
    }

    private fun resolveHeadingRelativeToDirection(instruction: Instruction, heading: Heading): Heading {
        return when (instruction.first) {
            'L' -> heading.left(instruction.second)
            'R' -> heading.right(instruction.second)
            else -> heading
        }
    }
}
