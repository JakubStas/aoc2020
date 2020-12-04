import java.util.stream.Stream

class InputDataReader {

    fun readLines(day: String): Stream<String> =
        javaClass.getResourceAsStream("/$day/input.txt").bufferedReader().lines()
}
