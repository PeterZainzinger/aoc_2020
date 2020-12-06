import java.io.File
import java.lang.Integer.parseInt

private typealias INPUT = List<Line>
private typealias OUTPUT = Int


fun createLine(input: String) = Line(input.substring(0, 7), input.substring(7, 10))

data class Line(val rowsCode: String, val columnCode: String) {

    val rowDigits = rowsCode.map {
        when (it) {
            'F' -> 0
            'B' -> 1
            else -> throw RuntimeException("invalid char $it")
        }
    }
    val columnDigits = columnCode.map {
        when (it) {
            'L' -> 0
            'R' -> 1
            else -> throw RuntimeException("invalid char $it")
        }
    }

    fun seatID() = rowDigits.toInt() * 8 + columnDigits.toInt()
}

fun List<Int>.toInt() =
    parseInt(joinToString(""),2)


fun day5() {
    val input = File("inputs/5.txt").readLines().map { createLine(it) }
    //println(createLine("BFFFBBFRRR").seatID())
    println(exercise1(input))
    println(exercise2(input))
}


private fun exercise1(input: INPUT)  =
    input.map { it.seatID() }.maxOrNull()

private fun exercise2(input: INPUT): Set<Int> {
    val allIds = input.map { it.seatID() }
    val min = allIds.min()!!
    val max = allIds.max()!!
    return (min..max).toSet().minus(allIds)
}

