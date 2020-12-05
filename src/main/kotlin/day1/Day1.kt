package day1

import InputDataReader
import java.util.stream.Collectors

fun main() {
    val input = loadInputData()

    part1(input)
    part2(input)
}

fun part1(input: List<Int>) {
    val indexPair = twoSum(input, 2020)
    println("[part1] Indexes: $indexPair")

    val firstValue = input[indexPair.first]
    val secondValue = input[indexPair.second]
    println("[part1] Check sum: $firstValue + $secondValue = ${firstValue + secondValue}")

    val result = firstValue * secondValue
    println("[part1] Result: $result")
}

fun part2(input: List<Int>) {
    val indexList = threeSum(input, 2020)
    println("[part2] Indexes: ${indexList[0]}, ${indexList[1]}, ${indexList[2]}")

    val firstValue = input[indexList[0]]
    val secondValue = input[indexList[1]]
    val thirdValue = input[indexList[2]]
    val sum = firstValue + secondValue + thirdValue
    println("[part2] Check sum: $firstValue + $secondValue + $thirdValue = $sum")

    val result = firstValue * secondValue * thirdValue
    println("[part2] Result: $result")
}

fun loadInputData() =
    InputDataReader().readLines("day1").map { it.toInt() }.collect(Collectors.toList()).toMutableList()

fun twoSum(nums: List<Int>, target: Int, indexToSkip: Int = -1): Pair<Int, Int> {
    val complementToIndexMap = mutableMapOf<Int, Int>()

    for ((index, num) in nums.withIndex()) {
        val shouldProcess = indexToSkip != index || indexToSkip == -1

        if (shouldProcess && num <= target) {
            if (complementToIndexMap.containsKey(num)) {
                return Pair(complementToIndexMap.getValue(num), index)
            }

            complementToIndexMap[target - num] = index
        }
    }

    throw IllegalArgumentException("There is no solution in the provided input data!")
}

fun threeSum(nums: List<Int>, target: Int): List<Int> {
    val complementToIndexMap = mutableMapOf<Int, Int>()

    for ((index, num) in nums.withIndex()) {
        complementToIndexMap[target - num] = index
    }

    for ((targetForTwoSum, indexToIgnore) in complementToIndexMap) {
        try {
            val twoSumPair = twoSum(nums, targetForTwoSum, indexToIgnore)

            return listOf(indexToIgnore, twoSumPair.first, twoSumPair.second)
        } catch (e: IllegalArgumentException) {
            // no solution found, move on to the next one
        }
    }

    throw IllegalArgumentException("There is no solution in the provided input data!")
}