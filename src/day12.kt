import java.io.File
import kotlin.math.abs

data class Position(val x: Int, val y: Int) {
    operator fun plus(dir: Direction) = Position(x + dir.dx, y + dir.dy)
    fun manhatten() = abs(x) + abs(y)
}

data class Direction(val dx: Int, val dy: Int) {
    operator fun times(value: Int) = Direction(dx * value, dy * value)
    operator fun plus(direction: Direction) = Direction(
        dx + direction.dx,
        dy + direction.dy
    )
}

private sealed class MoveInstruction {
    data class Direct(val direction: Direction, val value: Int) : MoveInstruction()
    data class Forward(val value: Int) : MoveInstruction()
    data class ChangeDir(val value: Int) : MoveInstruction()
}

private fun parseMove(input: String): MoveInstruction {
    val value = input.substring(1 until input.length).toInt()
    return when (val action = input.first()) {
        'N' -> MoveInstruction.Direct(Direction(0, 1), value = value)
        'S' -> MoveInstruction.Direct(Direction(0, -1), value = value)
        'E' -> MoveInstruction.Direct(Direction(1, 0), value = value)
        'W' -> MoveInstruction.Direct(Direction(-1, 0), value = value)
        'L' -> MoveInstruction.ChangeDir(value)
        'R' -> MoveInstruction.ChangeDir(360 - value % 360)
        'F' -> MoveInstruction.Forward(value = value)
        else -> throw Exception("invalid dir $action")
    }
}

private fun calcEndPosition(
    instructions: List<MoveInstruction>,
    startPosition: Position,
    startDirection: Direction,
    f: (Position, Direction, MoveInstruction) -> Pair<Position, Direction>
) = instructions.fold(startPosition to startDirection) { acc, move -> f(acc.first, acc.second, move) }.first

private fun rotateLeft(value: Int, direction: Direction): Direction = when (value) {
    0 -> direction
    else -> rotateLeft(value - 90, Direction(-direction.dy, direction.dx))
}

private fun calcNextPosition(currentPosition: Position, currentDir: Direction, instruction: MoveInstruction) =
    when (instruction) {
        is MoveInstruction.Direct -> currentPosition + instruction.direction * instruction.value to currentDir
        is MoveInstruction.Forward -> currentPosition + currentDir * instruction.value to currentDir
        is MoveInstruction.ChangeDir -> currentPosition to rotateLeft(instruction.value, currentDir)
    }


private fun calcNextPosition2(currentPosition: Position, currentDir: Direction, instruction: MoveInstruction) =
    when (instruction) {
        is MoveInstruction.Direct -> currentPosition to (currentDir + instruction.direction * instruction.value)
        is MoveInstruction.Forward -> (currentPosition + currentDir * instruction.value) to currentDir
        is MoveInstruction.ChangeDir -> currentPosition to rotateLeft(instruction.value, currentDir)
    }

fun day12() {
    val input = File("inputs/12.txt").readLines().map(::parseMove)
    println(calcEndPosition(input, Position(0, 0), Direction(1, 0), ::calcNextPosition).manhatten())
    println(calcEndPosition(input, Position(0, 0), Direction(10, 1), ::calcNextPosition2).manhatten())
}
