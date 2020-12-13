package day12.part1

import day12.Heading

class Location(var xEastWest: Int = 0, var yNorthSouth: Int = 0, var heading: Heading = Heading.EAST) {

    override fun toString(): String {
        return "[$xEastWest, $yNorthSouth] : $heading"
    }
}
