package day6

import InputDataReader
import java.util.stream.Collectors

fun main() {
    val input = loadInputData()

    part1(input)
    part2(input)
}

fun part1(input: List<String>) {
    val cdfParser = CdfParser()

    input.fold(cdfParser) { parser, line -> parser.parse(line) }
    cdfParser.parse("")

    println("[part1] Sum of questions anyone answered with Yes: ${cdfParser.getSumOfQuestionsAnsweredWithYes()}")
}

fun part2(input: List<String>) {
    val cdfParser = CdfParser(strictlyEveryone = true)

    input.fold(cdfParser) { parser, line -> parser.parse(line) }
    cdfParser.parse("")

    println("[part2] Sum of questions everyone answered with Yes: ${cdfParser.getSumOfQuestionsAnsweredWithYes()}")
}

fun loadInputData(): List<String> = InputDataReader().readLines("day6").collect(Collectors.toList())

class CdfParser(
    private val strictlyEveryone: Boolean = false,
    private var sumOfQuestionsAnsweredWithYes: Int = 0,
    private val answeredQuestions: BooleanArray = BooleanArray(26) { strictlyEveryone }
) {

    fun parse(line: String): CdfParser {
        if (line == "") {
            onBlankLine()
        } else {
            onDataLine(line.toQuestionnaire())
        }

        return this
    }

    private fun onBlankLine() {
        sumOfQuestionsAnsweredWithYes += answeredQuestions.count { it }
        answeredQuestions.fill(strictlyEveryone)
    }

    private fun onDataLine(questionnaire: BooleanArray) = questionnaire.forEachIndexed { index, answered ->
        if (strictlyEveryone) {
            answeredQuestions[index] = answeredQuestions[index] && answered
        } else {
            answeredQuestions[index] = answeredQuestions[index] || answered
        }
    }


    fun getSumOfQuestionsAnsweredWithYes() = sumOfQuestionsAnsweredWithYes
}

fun String.toQuestionnaire(): BooleanArray {
    val questionnaire = BooleanArray(26) { false }

    for (character in this) {
        questionnaire[character.toInt() - 'a'.toInt()] = true
    }

    return questionnaire
}