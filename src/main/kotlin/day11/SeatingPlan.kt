package day11

typealias SeatingPlan = List<CharArray>

const val FLOOR = '.'
const val EMPTY_CHAIR = 'L'
const val OCCUPIED_CHAIR = '#'

fun SeatingPlan.rows() = indices

fun SeatingPlan.columns() = get(0).indices

fun SeatingPlan.isOccupiedSeat(row: Int, column: Int) = get(row)[column] == OCCUPIED_CHAIR

fun SeatingPlan.clone() = map { it.clone() }.toList()

fun SeatingPlan.matches(other: SeatingPlan) = flatMap { it.asIterable() } == other.flatMap { it.asIterable() }
