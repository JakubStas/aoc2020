package day4

import InputDataReader
import java.util.function.Function
import java.util.stream.Collectors

fun main() {
    val input = loadInputData()

    part1(input)
    part2(input)
}

fun part1(input: List<String>) {
    val foldParser = FoldParser()

    input.fold(foldParser) { parser, line -> parser.parseInFold(line) }
    // make sure to finish parsing by adding separator
    foldParser.parseInFold("")

    println("[part1] Number of valid passwords: ${foldParser.getNumberOfValidPassports()}")
}

fun part2(input: List<String>) {
    val foldParser = FoldParser(validationOn = true)

    input.fold(foldParser) { parser, line -> parser.parseInFold(line) }
    // make sure to finish parsing by adding separator
    foldParser.parseInFold("")

    println("[part2] Number of valid passwords: ${foldParser.getNumberOfValidPassports()}")
}

fun loadInputData(): List<String> = InputDataReader().readLines("day4").collect(Collectors.toList())

class PassportValidator {

    private fun createRangeCheckValidator(from: Int, to: Int) = Function<String, Boolean> {
        try {
            it.toInt() in from..to
        } catch (e: NumberFormatException) {
            // skip, not a number
            false
        }
    }

    private fun createHeightValidator() = Function<String, Boolean> {
        when {
            it.endsWith("cm") -> createRangeCheckValidator(150, 193).apply(it.removeSuffix("cm"))
            it.endsWith("in") -> createRangeCheckValidator(59, 76).apply(it.removeSuffix("in"))
            else -> false
        }
    }

    private fun createRegexpValidator(regex: String) = Function<String, Boolean>(Regex(regex)::matches)

    private val validation = mapOf(
        "byr" to createRangeCheckValidator(1920, 2002),
        "iyr" to createRangeCheckValidator(2010, 2020),
        "eyr" to createRangeCheckValidator(2020, 2030),
        "hgt" to createHeightValidator(),
        "hcl" to createRegexpValidator("#[a-f0-9]{6}"),
        "ecl" to createRegexpValidator("(amb|blu|brn|gry|grn|hzl|oth)"),
        "pid" to createRegexpValidator("[0-9]{9}"),
        "cid" to Function<String, Boolean> { true }
    )

    fun validate(fieldName: String, fieldValue: String): Boolean {
        return if (validation.containsKey(fieldName)) {
            validation[fieldName]!!.apply(fieldValue)
        } else {
            false
        }
    }
}

class FoldParser(
    private val validationOn: Boolean = false,
    private var validPasswordCount: Int = 0,
    private var invalidPasswordCount: Int = 0,
    private val encounteredFields: MutableSet<String> = mutableSetOf(),
    private val passportValidator: PassportValidator = PassportValidator()
) {

    fun parseInFold(line: String): FoldParser {
        if (line == "") {
            blankLineEncountered()
        } else {
            dataLineEncountered(line)
        }

        return this
    }

    private fun blankLineEncountered() {
        // assuming no validation is needed and only 8 of the valid fields were provided
        if (encounteredFields.size == 8 || (encounteredFields.size == 7 && !encounteredFields.contains("cid"))) {
            // mark passport valid
            validPasswordCount++
        } else {
            invalidPasswordCount++
        }

        encounteredFields.clear()
    }

    private fun dataLineEncountered(line: String) {
        line.splitToSequence(" ")
            .filter {
                if (validationOn) {
                    passportValidator.validate(it.split(":")[0], it.split(":")[1])
                } else {
                    true
                }
            }
            .forEach { encounteredFields.add(it.split(":")[0]) }
    }

    fun getNumberOfValidPassports() = validPasswordCount
}