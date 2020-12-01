import java.io.File
import java.lang.RuntimeException

fun day1() {
    val numbers = File("inputs/1_0.txt")
        .readLines()
        .map { Integer.parseInt(it) }
        .sorted()

    val (i, j) = findPair(numbers, 2020);
    println(i * j)
    println(findTriple(numbers, 2020).fold(1, { a, b -> a * b }))
}

private fun findPair(numbers: List<Int>, target: Int): Pair<Int, Int> {
    for (i in numbers) {
        for (j in numbers) {
            if (i + j == target) {
                return i to j
            }
        }
    }
    throw RuntimeException("not found")
}

private fun findTriple(numbers: List<Int>, target: Int): List<Int> {
    for (i in numbers) {
        for (j in numbers) {
            for (h in numbers) {
                if (i + j + h == target) {
                    return listOf(i, j, h)
                }
            }
        }
    }
    throw RuntimeException("not found")
}
