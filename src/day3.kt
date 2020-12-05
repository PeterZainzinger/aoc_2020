import java.io.File

fun day3() {
    val lines = File("inputs/3.txt")
        .readLines()
    println(countHits(lines))
    exercise2(lines)

}

fun countHits(lines: List<String>, right: Int = 3, down: Int = 1) = lines.mapIndexed { index, line ->
    when (index % down) {
        0 ->
            when (line[(index / down) * right % line.length]) {
                '#' -> 1
                else -> 0
            }
        else -> 0
    }
}.sum()


fun exercise2(lines: List<String>) {
    val res = listOf(
        1 to 1,
        3 to 1,
        5 to 1,
        7 to 1,
        1 to 2,
    ).map { (right, down) ->
        val hintCount = countHits(lines, right = right, down = down)
        println("$right / $down $hintCount")
        hintCount.toLong()
    }.fold(1L, { a, b -> a * b })
    println(res)

}
