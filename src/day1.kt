import java.io.File
import java.lang.RuntimeException

fun day1() {
    val numbers = File("inputs/1_0.txt")
        .readLines()
        .map { Integer.parseInt(it) }
        .sorted()

    val (i, j) = findPair(numbers, 2020);
    println(i * j)
    val (a, b, c) = findTriple(numbers, 2020)
    println(a * b * c)
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

private fun findTriple(numbers: List<Int>, target: Int): Triple<Int, Int, Int> {
    for (i in numbers) {
        for (j in numbers) {
            for (h in numbers) {
                if (i + j + h == target) {
                    return Triple(i, j, h)
                }
            }
        }
    }
    throw RuntimeException("not found")
}
