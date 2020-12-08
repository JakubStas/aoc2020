package day8

import InputDataReader
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

val ACCUMULATOR = AtomicInteger(0)

fun main() {
    val loadInputData = loadInputData()

    part1(loadInputData)
    part2(loadInputData)
}

fun part1(input: List<Instruction>) {
    try {
        processInstruction(instructionSet = input)
    } catch (e: CycleInExecution) {
        println("[part1] Value in the accumulator just before running the program terminated: ${ACCUMULATOR.get()}")
    }
}

fun part2(input: List<Instruction>) {
    val indicesOfInstructionsToToggle =
        input.filter { it.executionIndex > -1 && (it.type == InstructionType.JMP || it.type == InstructionType.NOP) }
            .sortedBy(Instruction::executionIndex)
            .map { it.executionIndex }

    for (indexOfInstructionToggle in indicesOfInstructionsToToggle) {
        try {
            input.scrapeExecutionMetadata()

            println(
                "[part2] Value in the accumulator just before running the program terminated: ${
                    processInstruction(
                        instructionSet = input,
                        toggleIndex = indexOfInstructionToggle
                    )
                }"
            )

            return
        } catch (e: CycleInExecution) {
            println("[part2] Toggling instruction with execution index $indexOfInstructionToggle failed! Trying the next one ...")
        }
    }
}

fun loadInputData(): List<Instruction> =
    InputDataReader().readLines("day8").map { it.toInstruction() }.collect(Collectors.toList())

fun List<Instruction>.scrapeExecutionMetadata() {
    forEach { it.executionIndex = -1 }
    ACCUMULATOR.set(0)
}

fun processInstruction(index: Int = 0, instructionSet: List<Instruction>, toggleIndex: Int = -1): Int {
    if (index == instructionSet.size) {
        return ACCUMULATOR.get()
    }

    var instruction = instructionSet[index]
    var toggleIndexToPassDown = toggleIndex

    // explore the alternative path by toggling the instruction
    if (index == toggleIndex) {
        toggleIndexToPassDown = -1

        if (instruction.type == InstructionType.NOP) {
            instruction = Instruction(InstructionType.JMP, instruction.value, instruction.executionIndex)
        } else if (instruction.type == InstructionType.JMP) {
            instruction = Instruction(InstructionType.NOP, instruction.value, instruction.executionIndex)
        }
    }

    return if (instruction.executionIndex != -1) {
        throw CycleInExecution()
    } else {
        processInstruction(instruction.execute(index), instructionSet, toggleIndexToPassDown)
    }
}

fun String.toInstruction(): Instruction {
    val instructionType = this.split(" ")[0]
    val instructionValue = this.split(" ")[1].toInt()

    return when (instructionType) {
        "acc" -> Instruction(InstructionType.ACC, instructionValue)
        "jmp" -> Instruction(InstructionType.JMP, instructionValue)
        "nop" -> Instruction(InstructionType.NOP, instructionValue)
        else -> throw IllegalArgumentException()
    }
}

class Instruction(var type: InstructionType, val value: Int, var executionIndex: Int = -1) {

    fun execute(index: Int): Int {
        executionIndex = index

        return when (type) {
            InstructionType.ACC -> {
                ACCUMULATOR.addAndGet(value)
                index + 1
            }
            InstructionType.JMP -> {
                index + value
            }
            InstructionType.NOP -> {
                index + 1
            }
        }
    }
}

enum class InstructionType { ACC, JMP, NOP }

class CycleInExecution : RuntimeException()