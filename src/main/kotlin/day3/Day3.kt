package day3

import InputDataReader
import java.util.stream.Collectors

fun main() {
    val levelsOfForest = loadInputData()

    part1(levelsOfForest)
    part2(levelsOfForest)
}

fun part1(levelsOfForest: List<String>) {
    val result = traverseSlopeAndGetTreeCount(listOf(3 to 1), levelsOfForest)
    println("[part1] Product of tree count: $result")
}

fun part2(levelsOfForest: List<String>) {
    val strategies = listOf(
        1 to 1,
        3 to 1,
        5 to 1,
        7 to 1,
        1 to 2
    )

    val result = traverseSlopeAndGetTreeCount(strategies, levelsOfForest)
    println("[part2] Product of tree count: $result")
}

fun traverseSlopeAndGetTreeCount(strategies: List<Pair<Int, Int>>, levelsOfForest: List<String>): Int {
    var result = 1

    for (strategy in strategies) {
        val treeCount = explorePathDown(strategy.first, strategy.second, levelsOfForest)
        result *= treeCount

        println("[right ${strategy.first}, down ${strategy.second}] Number of trees encountered: $treeCount")
    }

    return result
}

fun explorePathDown(stepsRight: Int, stepsDown: Int, levelsOfForest: List<String>): Int {
    var treeCount = 0
    val windowWidth = levelsOfForest[0].length

    val windowBoundaryRight = windowWidth - 1
    var index = 0
    var depth = 0

    while (depth < levelsOfForest.size - 1) {
        depth += stepsDown

        if (stepsRight + index > windowBoundaryRight) {
            // adjust the index to slide the window
            index = (stepsRight + index) - windowWidth
        } else {
            index += stepsRight
        }

        if (levelsOfForest[depth][index] == '#') {
            treeCount++
        }
    }

    return treeCount
}

fun loadInputData() = InputDataReader().readLines("day3").collect(Collectors.toList()).toMutableList()