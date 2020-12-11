package day11

class Simulation1(private val seatingPlan: List<CharArray>) {

    fun run() {
        var currentIteration = seatingPlan.clone()
        var nextIteration = runNextIteration(currentIteration)

        while (!currentIteration.matches(nextIteration)) {
            currentIteration = nextIteration
            nextIteration = runNextIteration(currentIteration)
        }

        val occupiedSeats = nextIteration.flatMap { it.asIterable() }.count { it == OCCUPIED_CHAIR }

        println("[part1] Number of occupied seats: $occupiedSeats")
    }

    private fun runNextIteration(seatingPlan: List<CharArray>): List<CharArray> {
        val nextIteration = seatingPlan.clone()

        for (row in seatingPlan.rows()) {
            for (column in seatingPlan.columns()) {
                when (seatingPlan[row][column]) {
                    EMPTY_CHAIR -> {
                        if (!seatingPlan.getImmediateSurroundingTiles(row, column)
                                .any { seatingPlan.isOccupiedSeat(it.first, it.second) }
                        ) {
                            nextIteration[row][column] = OCCUPIED_CHAIR
                        }
                    }
                    OCCUPIED_CHAIR -> {
                        if (seatingPlan.getImmediateSurroundingTiles(row, column)
                                .count { seatingPlan.isOccupiedSeat(it.first, it.second) } > 3
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

private fun SeatingPlan.getImmediateSurroundingTiles(row: Int, column: Int): Set<Pair<Int, Int>> {
    val surroundingTiles: MutableSet<Pair<Int, Int>> = mutableSetOf()

    val xs = listOf(row - 1, row, row + 1)
    val ys = listOf(column - 1, column, column + 1)

    for (x in xs) {
        for (y in ys) {
            surroundingTiles.add(Pair(x, y))
        }
    }

    return surroundingTiles
        .filter { tile -> tile != Pair(row, column) }
        .filter { tile -> tile.first in rows() }
        .filter { tile -> tile.second in columns() }
        .toSet()
}
