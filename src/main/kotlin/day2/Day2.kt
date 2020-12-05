package day2

import InputDataReader
import java.util.stream.Collectors
import kotlin.reflect.KClass

fun main() {
    part1()
    part2()
}

fun part1() {
    val numberOfValidPasswords = loadInputData(OldPasswordPolicy::class).filter(Password::isValid).count()
    println("[part1] Number of valid passwords: $numberOfValidPasswords")
}

fun part2() {
    val numberOfValidPasswords = loadInputData(TobogganPasswordPolicy::class).filter(Password::isValid).count()
    println("[part2] Number of valid passwords: $numberOfValidPasswords")
}

fun loadInputData(policyType: KClass<out PasswordPolicy>): List<Password> =
    InputDataReader().readLines("day2")
        .map { parseOutPassword(it, policyType) }
        .collect(Collectors.toList())
        .toMutableList()

fun parseOutPolicy(inputLine: String, policyType: KClass<out PasswordPolicy>): PasswordPolicy {
    return when (policyType) {
        OldPasswordPolicy::class -> {
            val (occurrencesPart, character) = inputLine.split(" ")
            val (minOccurrences, maxOccurrences) = occurrencesPart.split("-")

            OldPasswordPolicy(minOccurrences.toInt(), maxOccurrences.toInt(), character[0])
        }
        TobogganPasswordPolicy::class -> {
            val (occurrencesPart, character) = inputLine.split(" ")
            val (firstIndex, secondIndex) = occurrencesPart.split("-")

            TobogganPasswordPolicy(firstIndex.toInt(), secondIndex.toInt(), character[0])
        }
        else -> throw IllegalArgumentException("Invalid password policy type!")
    }
}

fun parseOutPassword(inputLine: String, policyType: KClass<out PasswordPolicy>): Password {
    val (policyPart, passwordPart) = inputLine.split(":")

    return Password(passwordPart.trim(), parseOutPolicy(policyPart.trim(), policyType))
}

class OldPasswordPolicy(private val minOccurrences: Int, private val maxOccurrences: Int, private val character: Char) :
    PasswordPolicy(minOccurrences, maxOccurrences, character) {

    override fun evaluatePassword(password: String) =
        password.toCharArray().count { it == character } in minOccurrences..maxOccurrences
}

class TobogganPasswordPolicy(private val firstIndex: Int, private val secondIndex: Int, private val character: Char) :
    PasswordPolicy(firstIndex, secondIndex, character) {

    override fun evaluatePassword(password: String): Boolean {
        val firstIndexMatches = password[firstIndex - 1] == character
        val secondIndexMatches = password[secondIndex - 1] == character

        return firstIndexMatches xor secondIndexMatches
    }
}

abstract class PasswordPolicy(
    private val firstNumber: Int,
    private val secondNumber: Int,
    private val character: Char,
) {

    abstract fun evaluatePassword(password: String): Boolean

    override fun toString(): String {
        return "PasswordPolicy(firstNumber=$firstNumber, secondNumber=$secondNumber, character=$character)"
    }
}

class Password(private val text: String, private val policy: PasswordPolicy) {

    fun isValid(): Boolean {
        return policy.evaluatePassword(text)
    }

    override fun toString(): String {
        return "Password(text='$text', policy=$policy)"
    }
}