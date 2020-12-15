import java.io.File

fun day15() {
    val input = File("inputs/15.txt").readText().split(",").map { it.toInt() }
    println(findNth(input,2020))
    println(findNth(input,30000000))
}

private fun findNth(input: List<Int>, n: Int): Int {
    val currentMem =
        input.toList().mapIndexed { index, i -> i to index }.toMap().toMutableMap()
    var currentNum = input.last()
    var i = input.size
    while (i < n) {
        var newNum = when (val entry = currentMem[currentNum]) {
            null -> 0
            else -> i - entry - 1
        }
        currentMem[currentNum] = i - 1
        currentNum = newNum
        i++
    }
    return currentNum
}

// naive impl
private fun nextNum(past: List<Int>): Int {
    val last = past.last()
    val others = past.subList(0, past.size - 1)
    return when (val indexOf = others.lastIndexOf(last)) {
        -1 -> 0
        else -> past.size - indexOf - 1
    }
}