import java.io.File
import java.lang.Math.min
import java.lang.Math.pow
import kotlin.math.pow

var memory = mutableMapOf<Int, Long>()

fun day10() {
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
    println(count1 * count3)
    listOf(
        ::countArrangements to "my solution",
        ::redditSolution to "reddit",
        ::countArrangements to "my solution",
    ).forEach {(f,name)->
        val start = System.nanoTime()
        val counted = f(input)
        println("name: ${name.padEnd(15)} result: $counted time:(${(System.nanoTime() - start) / (10.0.pow(6.0))})")
        memory = mutableMapOf()
    }

}


fun countArrangements(input: List<Int>) = allArrangements(0, options = input + listOf(input.max()!! + 3))

fun allArrangements(last: Int, options: List<Int>): Long = when (val next = options.firstOrNull()) {
    null -> 1
    else -> {
        val inputId = (last to options).hashCode()
        when (val m = memory[inputId]) {
            null -> {
                val diff = next - last
                val res = when {
                    diff < 3 -> {
                        // two options skip or take
                        val skipOption = allArrangements(last, options.subList(1, options.size))
                        val takeOption = allArrangements(next, options.subList(1, options.size))
                        skipOption + takeOption
                    }
                    // we have to take it
                    diff == 3 -> allArrangements(next, options.subList(1, options.size))
                    else -> 0
                }
                memory[inputId] = res
                res
            }
            else -> m
        }
    }
}

fun redditSolution(input: List<Int>): Long {
    val adapters = input.sorted().toMutableList()
    adapters.add(0, 0)
    adapters.add(adapters.last() + 3)

    val options = LongArray(adapters.size)
    options[options.size - 2] = 1

    for (i in adapters.size - 3 downTo 0) {
        var currentOptions = 0L
        for (j in i until min(i + 4, adapters.size)) {
            if (adapters[j] - adapters[i] <= 3) {
                ++currentOptions
                currentOptions += options[j] - 1
            }
        }
        options[i] = currentOptions
    }

    return options[0]
}
