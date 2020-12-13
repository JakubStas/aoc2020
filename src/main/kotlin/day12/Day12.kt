package day12

import InputDataReader
import day12.part1.ShipV1
import day12.part2.ShipV2
import java.util.stream.Collectors
import kotlin.math.abs

typealias Instruction = Pair<Char, Int>

fun main() {
    val instructions = loadInputData()

    part1(instructions)
    part2(instructions)
}

fun part1(instructions: List<Instruction>) {
    val ferry = instructions.fold(ShipV1()) { ferry, instruction -> ferry.follow(instruction) }
    val manhattanDistance = abs(ferry.location.xEastWest) + abs(ferry.location.yNorthSouth)

    println("[part1] Manhattan distance between the ship's location and the ship's starting position: $manhattanDistance")
}

fun part2(instructions: List<Instruction>) {
    val ferry = instructions.fold(ShipV2()) { ferry, instruction -> ferry.follow(instruction) }
    val manhattanDistance = abs(ferry.location.xEastWest) + abs(ferry.location.yNorthSouth)

    println("[part2] Manhattan distance between the ship's location and the ship's starting position: $manhattanDistance")
}

fun loadInputData(): List<Instruction> = InputDataReader().readLines("day12")
    .map { Instruction(it.first(), it.slice(1 until it.length).toInt()) }
    .collect(Collectors.toList())
