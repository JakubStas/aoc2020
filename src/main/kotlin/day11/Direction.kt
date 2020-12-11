package day11

import day11.Bound.*

enum class Direction(private val directive: Pair<Bound, Bound>) {
    UP(LOWER to EMPTY),
    UP_RIGHT(LOWER to UPPER),
    RIGHT(EMPTY to UPPER),
    DOWN_RIGHT(UPPER to UPPER),
    DOWN(UPPER to EMPTY),
    DOWN_LEFT(UPPER to LOWER),
    LEFT(EMPTY to LOWER),
    UP_LEFT(LOWER to LOWER);

    fun getRowDirective() = directive.first

    fun getColumnDirective() = directive.second
}
