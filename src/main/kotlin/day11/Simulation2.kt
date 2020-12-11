package day11

class Simulation2(private val seatingPlan: List<CharArray>) {

    fun run() {
        var currentIteration = seatingPlan.clone()
        var nextIteration = runNextIteration(currentIteration)

        while (!currentIteration.matches(nextIteration)) {
            currentIteration = nextIteration
            nextIteration = runNextIteration(currentIteration)
        }

        val occupiedSeats = nextIteration.flatMap { it.asIterable() }.count { it == OCCUPIED_CHAIR }

        println("[part2] Number of occupied seats: $occupiedSeats")
    }

    private fun runNextIteration(seatingPlan: List<CharArray>): List<CharArray> {
        val nextIteration = seatingPlan.clone()

        for (row in seatingPlan.rows()) {
            for (column in seatingPlan.columns()) {
                when (seatingPlan[row][column]) {
                    EMPTY_CHAIR -> {
                        if (!seatingPlan.getSurroundingTilesMultiDirectional(row, column)
                                .any { seatingPlan.isOccupiedSeat(it.first, it.second) }
                        ) {
                            nextIteration[row][column] = OCCUPIED_CHAIR
                        }
                    }
                    OCCUPIED_CHAIR -> {
                        if (seatingPlan.getSurroundingTilesMultiDirectional(row, column)
                                .count { seatingPlan.isOccupiedSeat(it.first, it.second) } > 4
                        ) {
                            nextIteration[row][column] = EMPTY_CHAIR
                        }
                    }
                    FLOOR -> {
                        // does not change
                    }
                }
            }
        }

        return nextIteration
    }
}

private fun SeatingPlan.getSurroundingTilesMultiDirectional(row: Int, column: Int) = Direction.values()
    .map { performCheckInOneDirection(row, column, it) }
    .filter { it.first != -1 && it.second != -1 }
    .toSet()

private fun SeatingPlan.performCheckInOneDirection(
    row: Int,
    column: Int,
    direction: Direction
): Pair<Int, Int> {
    val rowBoundaryToRowIncrement = when (direction.getRowDirective()) {
        Bound.LOWER -> 0 to -1
        Bound.UPPER -> rows().last to 1
        Bound.EMPTY -> -1 to 0
    }

    val columnBoundaryToColumnIncrement = when (direction.getColumnDirective()) {
        Bound.LOWER -> 0 to -1
        Bound.UPPER -> columns().last to 1
        Bound.EMPTY -> -1 to 0
    }

    val isRowIndexOnTheEdgeOfIteration = (row == rowBoundaryToRowIncrement.first) &&
            ((rowBoundaryToRowIncrement.first == 0 && rowBoundaryToRowIncrement.second < 0) ||
                    (rowBoundaryToRowIncrement.first == rows().last && rowBoundaryToRowIncrement.second > 0))

    val isColumnIndexOnTheEdgeOfIteration = (column == columnBoundaryToColumnIncrement.first) &&
            ((columnBoundaryToColumnIncrement.first == 0 && columnBoundaryToColumnIncrement.second < 0) ||
                    (columnBoundaryToColumnIncrement.first == columns().last && columnBoundaryToColumnIncrement.second > 0))

    if (isRowIndexOnTheEdgeOfIteration || isColumnIndexOnTheEdgeOfIteration) {
        // skip
        return Pair(-1, -1)
    } else {
        var rowIndex = row
        var columnIndex = column

        var tileValue = FLOOR

        while (tileValue == FLOOR && rowIndex != rowBoundaryToRowIncrement.first && columnIndex != columnBoundaryToColumnIncrement.first) {
            rowIndex += rowBoundaryToRowIncrement.second
            columnIndex += columnBoundaryToColumnIncrement.second

            tileValue = this[rowIndex][columnIndex]
        }

        return Pair(rowIndex, columnIndex)
    }
}
