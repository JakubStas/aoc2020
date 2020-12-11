package day11

import InputDataReader
import java.util.stream.Collectors

fun main() {
    val input = loadInputData()

    Simulation1(input).run()
    Simulation2(input).run()
}

fun loadInputData(): List<CharArray> =
    InputDataReader().readLines("day11").map { it.toCharArray() }.collect(Collectors.toList())
