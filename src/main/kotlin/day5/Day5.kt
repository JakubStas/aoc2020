package day5

import InputDataReader
import java.util.stream.Collectors

val ROWS = IntArray(128) { it }
val COLUMNS = IntArray(8) { it }

fun main() {
    val input = loadInputData()

    part1(input)
    part2(input)
}

fun part1(input: List<CharArray>) {
    val maxSeatId = input.map { getSeatId(findRow(location = it), findColumn(location = it)) }.maxOrNull()
    println("[part1] Maximum seat ID: $maxSeatId")
}

fun part2(input: List<CharArray>) {
    val myLocation = input.map { findRow(location = it) to findColumn(location = it) }
        .groupBy { it.first }
        .map { it -> it.key to it.value.map { it.second } }
        .filter { it.second.size == COLUMNS.size - 1 }

    if (myLocation.isEmpty()) {
        throw IllegalArgumentException()
    } else {
        val row = myLocation[0].first
        val column = COLUMNS.filter { !myLocation[0].second.contains(it) }[0]

        println("[part2] My seat ID: ${getSeatId(row, column)}")
    }
}

fun loadInputData(): List<CharArray> =
    InputDataReader().readLines("day5").map { it.toCharArray() }.collect(Collectors.toList())

fun findRow(input: IntArray = ROWS, location: CharArray, locationIndex: Int = 0): Int {
    return if (locationIndex == location.size - 3) {
        input[0]
    } else {
        findRow(
            when (location[locationIndex]) {
                'F' -> input.copyOfRange(0, input.size / 2)
                'B' -> input.copyOfRange(input.size / 2, input.size)
                else -> throw IllegalArgumentException()
            }, location, locationIndex + 1
        )
    }
}

fun findColumn(input: IntArray = COLUMNS, location: CharArray, locationIndex: Int = 7): Int {
    return if (locationIndex == location.size) {
        input[0]
    } else {
        findColumn(
            when (location[locationIndex]) {
                'L' -> input.copyOfRange(0, input.size / 2)
                'R' -> input.copyOfRange(input.size / 2, input.size)
                else -> throw IllegalArgumentException()
            }, location, locationIndex + 1
        )
    }
}

fun getSeatId(row: Int, column: Int) = row * 8 + column