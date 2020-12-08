package day7

import InputDataReader
import java.util.stream.Collectors

val edges = mutableListOf<MutableList<Int>>()
val vertices = mutableListOf<String>()

fun debugVertices() = vertices.forEachIndexed { index, bagType -> println("$index - $bagType") }

fun debugEdges() {
    println()

    (0 until vertices.size).forEach { print(" $it ") }
    println()

    repeat(vertices.size) { print("---") }
    println()

    edges.forEachIndexed { vertexId, vertexEdges ->
        println(
            "${
                vertexEdges.fold(StringBuffer()) { text, edge ->
                    text.append(
                        " $edge "
                    )
                }
            } - $vertexId (${vertices[vertexId]})"
        )
    }
}

fun main() {
    loadInputData()

    debugEdges()

    part1()
    part2()
}


private fun rec(bagTypes: List<Pair<String, Int>>): Int {
    return if (bagTypes.isEmpty()) {
        0
    } else {
        bagTypes.fold(0) { count, pair -> count + pair.second + pair.second * rec(getBagsThatAreImmediatelyContainedBy(pair.first)) }
    }
}

fun part2() {

    println("[part2] Sum of bag types that must be contained by 'shiny gold': ${rec(getBagsThatAreImmediatelyContainedBy("shiny gold"))}")
}

fun part1() {
    debugVertices()
    debugEdges()

    val bags = mutableSetOf<String>()
    val bagTypesToCheck = ArrayDeque<String>()
    bagTypesToCheck.add("shiny gold")

    // no check for cycles in this graph, yolo!
    while (bagTypesToCheck.isNotEmpty()) {
        val parentVerticesToTraverse = getBagsThatCanImmediatelyContain(bagTypesToCheck.removeFirst())
        bags.addAll(parentVerticesToTraverse)

        parentVerticesToTraverse
            .filter { !bagTypesToCheck.contains(it) }
            .forEach(bagTypesToCheck::add)
    }

    println("[part1] Sum of bag types that can contain 'shiny gold': ${bags.size}")
}

fun loadInputData() =
    InputDataReader().readLines("day7")
        .map { it.substring(0, it.length - 1) }
        .map { processInputIntoMatrix(it) }
        .collect(Collectors.toList())

fun processInputIntoMatrix(line: String) {
    val outerBagType = line.split("bags contain")[0].trim()
    val indexOfOuterBagType = getIndexOrRegisterBagType(outerBagType)

    val containedBagsLine = line.split("bags contain")[1].trim()

    if (containedBagsLine.contains("no other bags")) {
        // no need to do anything for no other bags
        return
    }

    containedBagsLine.split(',').forEach { rawRule ->
        val ruleWithUnits = rawRule.trim()

        val rule = if (ruleWithUnits.endsWith("bags")) {
            ruleWithUnits.substring(0, ruleWithUnits.length - 5)
        } else {
            ruleWithUnits.substring(0, ruleWithUnits.length - 4)
        }

        val quantity = rule.substring(0, rule.indexOf(' ')).toInt()
        val bagType = rule.substring(rule.indexOf(' '), rule.length).trim()

        val indexOfBagType = getIndexOrRegisterBagType(bagType)
        edges[indexOfOuterBagType][indexOfBagType] = quantity
    }
}

private fun getIndexOrRegisterBagType(bagType: String): Int {
    if (!vertices.contains(bagType)) {
        // register new bag type
        vertices.add(bagType)
        // add graph edge option to link to it from other bags
        edges.forEach { it.add(0) }
        // add its own links
        edges.add(MutableList(vertices.size) { 0 })
    }

    return vertices.indexOf(bagType)
}

private fun getBagsThatCanImmediatelyContain(bagType: String): List<String> {
    val leafBagTypeIndex = getIndexOrRegisterBagType(bagType)
    val results = mutableListOf<String>()

    for ((vertexIndex, vertexEdges) in edges.withIndex()) {
        if (vertexEdges[leafBagTypeIndex] > 0) {
            results.add(vertices[vertexIndex])
        }
    }

    return results
}

private fun getBagsThatAreImmediatelyContainedBy(bagType: String): List<Pair<String, Int>> {
    val rootBagTypeIndex = getIndexOrRegisterBagType(bagType)
    val results = mutableListOf<Pair<String, Int>>()

    for ((vertexIndex, edgeWeight) in edges[rootBagTypeIndex].withIndex()) {
        if (edgeWeight > 0) {
            results.add(Pair(vertices[vertexIndex], edgeWeight))
        }
    }

    return results
}