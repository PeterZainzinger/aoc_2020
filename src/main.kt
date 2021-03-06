import java.io.File
import kotlin.math.pow

fun main() = all()
//fun main() = day25()

fun all() {
    val tasks = listOf(
        ::day1, ::day2, ::day3, ::day4, ::day5, ::day6, ::day7,
        ::day8, ::day9, ::day10, ::day11, ::day12, ::day13, ::day14,
        ::day15, ::day16, ::day17, ::day18, ::day19, ::day20,
        ::day21, ::day22, ::day23, ::day24, ::day25
    )
    val startTotal = System.nanoTime()
    tasks
        .mapIndexed { index, f -> index + 1 to f }
        .forEach { (i, f) ->
            println("====== execute ${i} =============")
            val start = System.nanoTime()
            f()
            val end = System.nanoTime()
            println("taken === ${((end - start) / 10.0.pow(6))}")
        }

    val endTotal = System.nanoTime()
    println("total taken === ${((endTotal - startTotal) / 10.0.pow(6))}")
}
