import java.io.File
import java.lang.Integer.parseInt

private typealias INPUT7 = List<Pair<String, List<Pair<Int, String>>>>

fun day7() {
    val input = File("inputs/7.txt").readLines()
        .map {
            val colorInfo = it.split(" bags contain ")
            val type = colorInfo[0]
            val content = colorInfo[1].split(",").map {
                when (it) {
                    "no other bags." -> 0 to ""
                    else -> {
                        val input = it.replace(" bags", "").replace("bag", "").replace(".", "").replace(",", "").trim()
                        val many = parseInt(input.substring(0..0))
                        val info = input.substring(1 until input.length).trim()
                        many to info
                    }
                }
            }
            type to content
        }
    println(findParents(input, "shiny gold").size)
    println(countChilds(input.toMap(), "shiny gold"))

}

private fun findParents(input: INPUT7, search: String): Set<String> {
    val parentsDirectly = input.filter { it.second.any { inner -> inner.second == search } }.map { it.first }.toSet()
    val higher = parentsDirectly.flatMap { findParents(input, it) }.toSet()
    return parentsDirectly + higher
}

private fun countChilds(input: Map<String, List<Pair<Int, String>>>, search: String): Int =
    when (val entry = input[search]) {
        null -> 0
        else -> entry.map {
            it.first * (countChilds(input, it.second) + 1)
        }.sum()
    }
