package day12.part2

class Location(var xEastWest: Int = 0, var yNorthSouth: Int = 0) {

    override fun toString(): String {
        return "($xEastWest, $yNorthSouth)"
    }
}
