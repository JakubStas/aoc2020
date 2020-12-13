package day12.part2

class Waypoint(var xEastWest: Int = 10, var yNorthSouth: Int = -1) {

    override fun toString(): String {
        return "[$xEastWest, $yNorthSouth]"
    }
}
