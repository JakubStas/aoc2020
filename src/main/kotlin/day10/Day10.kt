package day10

import InputDataReader
import java.util.stream.Collectors

const val JOLTAGE_RANGE = 3

fun main() {
    val input = loadInputData()
    val myDevice = input.last() + JOLTAGE_RANGE

    val adaptersAndDevices = input.toMutableList()
    adaptersAndDevices.add(myDevice)

    part1(adaptersAndDevices)
    part2(adaptersAndDevices)
}

fun part1(adaptersAndDevices: List<Long>) {
    val meter = adaptersAndDevices.fold(JoltageMeter()) { meter, thing -> meter.measureDevice(thing) }

    println("[part1] The number of 1-jolt differences multiplied by the number of 3-jolt differences is ${meter.oneJoltDiffCount * meter.threeJoltDiffCount}")
}

fun part2(adaptersAndDevices: List<Long>) {
    // trail is a unique sequence of adapters from the port (0) to another device (adapter or my device)
    // trail-space is a space grouping all unique trails from the port (0) to another device (adapter or my device)
    //   -> key is the end of the trail that they all share
    //   -> value is the number of unique trails within the space
    val trailSpaces: MutableMap<Long, Long> = mutableMapOf()
    val myDevice = adaptersAndDevices.last()

    for (thing in adaptersAndDevices) {
        advanceBySingleDevice3(thing, trailSpaces)
    }

    println("[part2] The total number of distinct ways you can arrange the adapters to connect the charging outlet to my device is: ${trailSpaces[myDevice]}")
}

fun loadInputData(): List<Long> =
    InputDataReader().readLines("day10").map { it.toLong() }.sorted().collect(Collectors.toList())

fun getNumberOfUniqueTrailsSoFar(trailSpaces: MutableMap<Long, Long>) = trailSpaces.values.sum()

fun MutableMap<Long, Long>.getEndNumbersOfTrailSpacesThatCantBeUsedWith(thing: Long) =
    filter { trailSpace -> thing !in trailSpace.key..trailSpace.key + JOLTAGE_RANGE }.keys

fun advanceBySingleDevice3(thing: Long, trailSpaces: MutableMap<Long, Long>) {
    if (thing in 0..JOLTAGE_RANGE) {
        // add a brand new trail space for thing
        trailSpaces[thing] = 1L + if (trailSpaces.isEmpty()) {
            // if there no pre-existing trail spaces, add only the single trail
            // for example the first trail space for thing 1 would be <1, 1>
            0L
        } else {
            // if there are any pre-existing trail spaces, use them to create new trail spaces
            // for example the second trail space for thing 2 would be <2, 1>
            // however, all trail spaces so far are:
            // <1,1> - (0,1)
            // <2,2> - (0,2); (0,1,2)
            getNumberOfUniqueTrailsSoFar(trailSpaces)
        }
    } else {
        // remove trail spaces that can't be used with thing
        val trailSpacesToRemove = trailSpaces.getEndNumbersOfTrailSpacesThatCantBeUsedWith(thing)
        trailSpacesToRemove.forEach { trailSpaces.remove(it) }

        // if there are any pre-existing trail spaces, use them to create new trail spaces
        // for example starting with trail space <7, 4> and processing thing 10 would reduce them to:
        // <7,4> - (0, 1, 4, 7); (0, 1, 4, 5, 7); (0, 1, 4, 6, 7); (0, 1, 4, 5, 6, 7)
        // <10,4> - (0, 1, 4, 7, 10); (0, 1, 4, 5, 7, 10); (0, 1, 4, 6, 7, 10); (0, 1, 4, 5, 6, 7, 10)
        trailSpaces[thing] = getNumberOfUniqueTrailsSoFar(trailSpaces)
    }
}

class JoltageMeter(
    var currentJoltage: Long = 0,
    var oneJoltDiffCount: Long = 0,
    var threeJoltDiffCount: Long = 0
) {
    fun measureDevice(device: Long): JoltageMeter {
        when (device - currentJoltage) {
            3L -> threeJoltDiffCount++
            1L -> oneJoltDiffCount++
        }

        currentJoltage = device

        return this
    }
}