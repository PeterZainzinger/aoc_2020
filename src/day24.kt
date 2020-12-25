import java.io.File

data class Vector(val dx: Int, val dy: Int) {
    operator fun plus(other: Vector) = Vector(dx + other.dx, dy + other.dy)

}
data class DirectionInstruction(val prefix: String, val direction: Vector) {
    companion object {
        val all = listOf(
            DirectionInstruction("e", Vector(2, 0)),
            DirectionInstruction("ne", Vector(1, 1)),
            DirectionInstruction("nw", Vector(-1, 1)),
            DirectionInstruction("w", Vector(-2, 0)),
            DirectionInstruction("sw", Vector(-1, -1)),
            DirectionInstruction("se", Vector(1, -1)),
        )
    }
}

private fun allNeighbors(vector: Vector) = DirectionInstruction.all.map { it.direction + vector }

private fun parseDirectionLine(line: String): List<DirectionInstruction> {
    val res = mutableListOf<DirectionInstruction>()
    var remaining = line
    while (remaining.isNotEmpty()) {
        val d = DirectionInstruction.all.first { remaining.startsWith(it.prefix) }
        res.add(d)
        remaining = remaining.substring(d.prefix.length)
    }
    return res
}

fun day24() {
    val input = File("inputs/24.txt").readLines().map { parseDirectionLine(it) }
    var flippedStates = mutableMapOf<Vector, Boolean>()
    input.forEach { identifier ->
        val resultingId = identifier.fold(Vector(0, 0), { acc, new -> acc + new.direction })
        val currentState = flippedStates.getOrDefault(resultingId, false)
        flippedStates[resultingId] = !currentState
    }
    println(flippedStates.values.filter { it }.size)
    var day = 0
    while (day < 100) {
        val allKeysToConsider = flippedStates.keys.flatMap { allNeighbors(it) }.toSet()
        flippedStates = allKeysToConsider.map { key ->
            val blackNeigbors = allNeighbors(key).filter { flippedStates[it] == true }.size
            val isBlack = flippedStates[key] ?: false
            key to when {
                isBlack -> blackNeigbors in 1..2
                else -> blackNeigbors == 2
            }
        }.toMap().toMutableMap()
        day++
    }
    println(flippedStates.values.filter { it }.size)
}