import java.io.File
import java.lang.Integer.parseInt

private typealias INPUT6 = List<List<String>>

fun day6() {
    val input = File("inputs/6.txt").readText().split("\n\n").map {
        it.split("\n")
    }
    println(exercise1(input))
    println(exercise2(input))
}

val alphabet = "abcdefghijklmniopqrstuvwxyz".toSet()

private fun charactersOfAnswers(input: INPUT6, initial: Set<Char>, operation: (Set<Char>, Set<Char>) -> Set<Char>) =
    input.map { items ->
        items.map { it.toCharArray().toSet() }.fold(initial = initial, operation = operation).size
    }.sum()

private fun exercise1(input: INPUT6) = charactersOfAnswers(input, emptySet()) { a, b -> a union b }
private fun exercise2(input: INPUT6) = charactersOfAnswers(input, alphabet) { a, b -> a intersect b }




