package day9

import InputDataReader
import java.util.concurrent.atomic.AtomicLong
import java.util.stream.Collectors
import kotlin.system.exitProcess

fun main() {
    val input = loadInputData()

    part2(input, part1(input))
}

fun part1(xmasNumberTape: List<Long>): Long {
    val preamble = Preamble(5)
    val number = xmasNumberTape.find { preamble.addValidNumberOrReturnInvalidOne(it) != -1L }!!

    println("[part1] The number breaking the property encoded in preamble is: $number")

    return number
}

fun part2(xmasNumberTape: List<Long>, target: Long) {
    xmasNumberTape.fold(mutableMapOf<AtomicLong, MutableSet<Long>>()) { sumOfMembersToMembersMap, number -> doCacheStuff(sumOfMembersToMembersMap, number, target) }
}

fun loadInputData(): List<Long> = InputDataReader().readLines("day9").map { it.toLong() }.collect(Collectors.toList())

fun doCacheStuff(
    sumOfMembersToMembersMap: MutableMap<AtomicLong, MutableSet<Long>>,
    number: Long,
    target: Long
): MutableMap<AtomicLong, MutableSet<Long>> {
    // update all candidate sum sequences
    sumOfMembersToMembersMap.forEach { (sumOfMembers, members) ->
        run {
            sumOfMembers.addAndGet(number)
            members.add(number)
        }
    }

    // add a new sum sequence starting with number
    sumOfMembersToMembersMap[AtomicLong(number)] = mutableSetOf(number)

    // remove all the sum sequences that overshot the target
    val updatedMap = sumOfMembersToMembersMap.filter { it.key.get() <= target }.toMutableMap()

    val targetSumOfMembersToMembers = updatedMap.entries.find { it.key.get() == target }

    if (targetSumOfMembersToMembers == null) {
        return updatedMap
    } else {
        println("[part2] The encryption weakness is: ${targetSumOfMembersToMembers.value.min()!! + targetSumOfMembersToMembers.value.max()!!}")
        exitProcess(0)
    }
}

class Preamble(private val preambleSize: Int, private val numbers: MutableList<Long> = mutableListOf()) {

    private var startIndex = 0

    private var endIndex = 0

    private fun isFullyInitialized() = numbers.size == preambleSize

    fun addValidNumberOrReturnInvalidOne(number: Long): Long {
        if (isFullyInitialized()) {
            val isSumOfTwoPrevious = isSumOfTwoPrevious(number)

            val insertionIndex = startIndex
            startIndex = (startIndex + 1) % preambleSize
            endIndex = (endIndex + 1) % preambleSize
            numbers[insertionIndex] = number

            return if (isSumOfTwoPrevious) {
                -1
            } else {
                number
            }
        } else {
            numbers.add(number)
            endIndex = numbers.size - 1
            return -1
        }
    }

    private fun isSumOfTwoPrevious(number: Long): Boolean {
        for (first in numbers) {
            for (second in numbers) {
                if (first != second && first + second == number) {
                    return true
                }
            }
        }

        return false
    }
}
