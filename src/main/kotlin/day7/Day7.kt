package day7

import InputDataReader

// matrix of edges
val packingRules = mutableListOf<MutableList<Int>>()

// list of vertices with their names
val bags = mutableListOf<String>()

val bagTypeRegex = "(.*) bags contain (.*)".toRegex()
val packingRuleRegex = """(\d+) (.*?) bags?""".toRegex()

fun main() {
    loadInputData()

    printOutBags()
    printOutPackingRules()

    part1()
    part2()
}

fun part1() {
    // results collection
    val bagsThatCanContainIt = mutableSetOf<String>()
    // processing stack
    val bagTypesToCheck = ArrayDeque<String>()
    bagTypesToCheck.add("shiny gold")

    // no check for cycles in this graph, yolo!
    while (bagTypesToCheck.isNotEmpty()) {
        val bagsInsideTheBagType = getBagsThatCanImmediatelyContain(bagTypesToCheck.removeFirst())
        // add contained bags to the results set
        bagsThatCanContainIt.addAll(bagsInsideTheBagType)

        // make sure to only add bag types that are not already queued up in the stack
        bagsInsideTheBagType.filter { !bagTypesToCheck.contains(it) }.forEach(bagTypesToCheck::add)
    }

    println("[part1] Sum of bag types that can contain 'shiny gold': ${bagsThatCanContainIt.size}")
}

fun part2() {
    println(
        "[part2] Sum of bag types that must be contained by 'shiny gold': ${
            calculateTotalNestingCapacityOf(
                getBagsThatAreImmediatelyContainedBy("shiny gold")
            )
        }"
    )
}

fun loadInputData() =
    InputDataReader().readLines("day7").map { it.substring(0, it.length - 1) }.forEach { processInputIntoMatrix(it) }

fun processInputIntoMatrix(line: String) {
    val outerBagType = bagTypeRegex.matchEntire(line)!!.groupValues[1]
    val indexOfOuterBagType = getIndexOrRegisterBagType(outerBagType)

    if (line.contains("no other bags")) {
        // no need to do anything for no other bags
        return
    }

    packingRuleRegex.findAll(bagTypeRegex.matchEntire(line)!!.groupValues[2])
        .map { it.groupValues[1].toInt() to it.groupValues[2] }.forEach { pair ->
            val quantity = pair.first
            val bagType = pair.second

            val indexOfBagType = getIndexOrRegisterBagType(bagType)
            packingRules[indexOfOuterBagType][indexOfBagType] = quantity
        }
}

private fun calculateTotalNestingCapacityOf(bagTypes: List<Pair<String, Int>>): Int {
    return if (bagTypes.isEmpty()) {
        0
    } else {
        bagTypes.fold(0) { totalNestingCapacity, packingRule ->
            totalNestingCapacity + packingRule.second + packingRule.second * calculateTotalNestingCapacityOf(
                getBagsThatAreImmediatelyContainedBy(
                    packingRule.first
                )
            )
        }
    }
}

private fun getBagsThatCanImmediatelyContain(bagType: String) =
    packingRules.withIndex()
        .filter { (_, packingRules) -> packingRules[getIndexOrRegisterBagType(bagType)] > 0 }
        .map { (bagIndex, _) -> bags[bagIndex] }

private fun getBagsThatAreImmediatelyContainedBy(bagType: String) =
    packingRules[getIndexOrRegisterBagType(bagType)].withIndex()
        .filter { (_, packingCount) -> packingCount > 0 }
        .map { (bagIndex, packingCount) -> Pair(bags[bagIndex], packingCount) }

private fun getIndexOrRegisterBagType(bagType: String): Int {
    if (!bags.contains(bagType)) {
        // register new bag type
        bags.add(bagType)
        // add slot for packing rules that contain this bag
        packingRules.forEach { it.add(0) }
        // add slot for packing rules of this bag
        packingRules.add(MutableList(bags.size) { 0 })
    }

    return bags.indexOf(bagType)
}

fun printOutBags() = bags.forEachIndexed { index, type -> println("$index - $type") }

fun printOutPackingRules() {
    println()

    // print bag indices
    (0 until bags.size).forEach { print(" $it ") }
    println()

    // print divider
    repeat(bags.size) { print("---") }
    println()

    // print the matrix of packing rules
    packingRules.forEachIndexed { bagIndex, packingRules ->
        println(
            "${
                packingRules.fold(StringBuffer()) { text, capacity ->
                    text.append(
                        " $capacity "
                    )
                }
            } - $bagIndex (${bags[bagIndex]})"
        )
    }

    println()
}
