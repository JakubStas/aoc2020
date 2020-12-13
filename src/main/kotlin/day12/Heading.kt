package day12

import day12.Coordinate.X_EAST_WEST
import day12.Coordinate.Y_NORTH_SOUTH
import kotlin.math.abs

enum class Heading(val axisToMovementDirection: Pair<Coordinate, Int>, private val index: Int) {
    NORTH(Y_NORTH_SOUTH to -1, 0),
    EAST(X_EAST_WEST to 1, 1),
    SOUTH(Y_NORTH_SOUTH to 1, 2),
    WEST(X_EAST_WEST to -1, 3);

    private fun getRotationIndexChange(degrees: Int) = degrees / 90

    private fun findByIndex(lookupIndex: Int) = values().first { it.index == lookupIndex }

    private fun performTurn(currentHeadingIndex: Int, rotationIndexChange: Int, isClockWise: Boolean): Heading {
        val endTurnPosition = if (isClockWise) {
            currentHeadingIndex + rotationIndexChange
        } else {
            currentHeadingIndex - rotationIndexChange
        }

        return findByIndex(abs(endTurnPosition % values().size))
    }

    fun left(degrees: Int): Heading {
        val indexAdjustedForNegatives = index + if (index - getRotationIndexChange(degrees) < 0) {
            values().size
        } else {
            0
        }

        return performTurn(indexAdjustedForNegatives, getRotationIndexChange(degrees), false)
    }

    fun right(degrees: Int) = performTurn(index, getRotationIndexChange(degrees), true)
}
