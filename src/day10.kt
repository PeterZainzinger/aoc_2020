import java.io.File

fun day10() {
    //val input = File("inputs/10.txt").readLines().map { Integer.parseInt(it) }.sorted()
    val input = File("inputs/10.txt").readLines().map { Integer.parseInt(it) }.sorted()
    val internal = input.max()!! + 3
    val inputWithStart = (listOf(0) + input)
    val diffs = inputWithStart.mapIndexed { index, i ->
        when (index) {
            inputWithStart.size - 1 -> 3
            else -> inputWithStart[index + 1] - i
        }
    }
    val count1 = diffs.filter { it == 1 }.size
    val count3 = diffs.filter { it == 3 }.size
    val countOther = diffs.filter { it != 3 && it != 1 }.size
//    println(countOther)
    //   println(count1 * count3)
    val counted = countArrangements(input)
    println(counted)
}

val memory = mutableMapOf<Int, Long>()

fun allArrangements(last: Int, options: List<Int>): Long {
    if (options.isEmpty()) {
        return 1
    } else {
        val next = options.first()
        val diff = next - last
        val inputId = (last to options).hashCode()
        val m = memory[inputId]
        if (m != null) {
            return m
        }
        val res = when {
            diff < 3 -> {
                // two options skip or take
                val skipOption = allArrangements(last, options.subList(1, options.size))
                val takeOption = allArrangements(next, options.subList(1, options.size))
                skipOption + takeOption
            }
            diff == 3 -> {
                // we have to take it
                allArrangements(next, options.subList(1, options.size))
            }
            else -> {
                0
            }
        }
        memory[inputId] = res
        return res
    }

}

fun countArrangements(input: List<Int>): Int {
    val internal = input.max()!! + 3
    val allOptions = allArrangements(0, options = input + listOf(internal))
    println(allOptions)
    return 0
}