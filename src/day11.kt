import java.io.File

sealed class SeatState(val char: Char) {
    object Empty : SeatState('L')
    object Occupied : SeatState('#')
    object Floor : SeatState('.')
}

fun readChar(item: Char) = when (item) {
    'L' -> SeatState.Empty
    '.' -> SeatState.Floor
    else -> throw Exception("what is this $item")
}

typealias State = List<List<SeatState>>

fun State.toPrettyString() = joinToString(separator = "\n") { it.joinToString(separator = "") { it.char.toString() } }

fun day11() {
    val input = File("inputs/11.txt")
        .readLines()
        .map { it.toCharArray().toList().map(::readChar) }
    println(countSeated(untilStable(input)))
    println(countSeated(untilStable2(input)))

}

private fun countSeated(state: State) = state.map { it.filter { it is SeatState.Occupied }.count() }.sum()

private fun untilStable(input: State): State {
    var before: State? = null
    var current = input
    while (before != current) {
        val beforeTmp = current
        current = calcNextState(current)
        before = beforeTmp
    }
    return current
}

private fun untilStable2(input: State): State {
    var before: State? = null
    var current = input
    while (before != current) {
        val beforeTmp = current
        current = calcNextState2(current)
        before = beforeTmp
    }
    return current
}

private fun getSafe(state: State, x: Int, y: Int): SeatState? = state.getOrNull(y)?.getOrNull(x)

private fun getAdjent(state: State, i: Int, j: Int) = listOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,
    0 to -1,
    0 to 1,
    1 to -1,
    1 to 0,
    1 to 1,
)

    .map { (dx, dy) -> (i + dx) to (j + dy) }
    .mapNotNull { (i, j) -> getSafe(state, i, j) }

private fun getAdjent2(state: State, i: Int, j: Int) = listOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,
    0 to -1,
    0 to 1,
    1 to -1,
    1 to 0,
    1 to 1,
).mapNotNull { (dx, dy) ->
    var offset = 1
    var res: SeatState? = null
    while (true) {
        val target = getSafe(state, i + offset * dx, j + offset * dy)
        if (target == null) {
            res = null
            break
        } else if (target != SeatState.Floor) {
            res = target
            break
        }
        offset++
    }
    res
}

fun calcNextState(state: State) = state.mapIndexed { y, row ->
    row.mapIndexed { x, item ->
        val adjent = getAdjent(state, x, y)
        val adjentSeated = adjent.filter { it is SeatState.Occupied }
        when (item) {
            is SeatState.Floor -> SeatState.Floor
            is SeatState.Empty -> when {
                adjentSeated.isEmpty() -> SeatState.Occupied
                else -> SeatState.Empty
            }
            is SeatState.Occupied -> when {
                adjentSeated.size > 3 -> SeatState.Empty
                else -> SeatState.Occupied
            }
        }
    }
}

fun calcNextState2(state: State) = state.mapIndexed { y, row ->
    row.mapIndexed { x, item ->
        val adjent = getAdjent2(state, x, y)
        val adjentSeated = adjent.filter { it is SeatState.Occupied }
        when (item) {
            is SeatState.Floor -> SeatState.Floor
            is SeatState.Empty -> when {
                adjentSeated.isEmpty() -> SeatState.Occupied
                else -> SeatState.Empty
            }
            is SeatState.Occupied -> when {
                adjentSeated.size > 4 -> SeatState.Empty
                else -> SeatState.Occupied
            }
        }
    }
}
