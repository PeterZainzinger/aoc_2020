import java.io.File
import java.lang.Math.abs

data class Position(val x: Int, val y: Int) {
    fun plus(dir: Direction, value: Int) = Position(x + value * dir.dx, y + value * dir.dy)
    fun manhatten() = abs(x) + abs(y)
}

data class Direction(val dx: Int, val dy: Int) {

    fun plus(direction: Direction, value: Int) = Direction(dx + value * direction.dx, dy + value * direction.dy)

}

sealed class MoveInstruction() {
    data class Direct(val direction: Direction, val value: Int) : MoveInstruction()
    data class Forward(val value: Int) : MoveInstruction()
    data class ChangeDir(val value: Int) : MoveInstruction()

}

private fun parseMove(input: String): MoveInstruction {
    val action = input.first()
    val value = input.substring(1 until input.length).toInt()
    return when (action) {
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

fun day12() {
    val input = File("inputs/12.txt")
        .readLines()
        .map { parseMove(it) }

    val (pos1, _) = input.fold(Position(0, 0) to Direction(1, 0), { acc, new ->
        val (currentPos, currentDir) = acc
        calcNextPosition(currentPos, currentDir, new)
    })
    println(pos1)
    println(pos1.manhatten())
    val (pos2, _) = input.fold(Position(0, 0) to Direction(10, 1), { acc, new ->
        val (currentPos, currentDir) = acc
        calcNextPosition2(currentPos, currentDir, new)
    })
    println(pos2)
    println(pos2.manhatten())
}

fun rotateLeft(value: Int, direction: Direction): Direction = when (value) {
    0 -> direction
    else -> {
        val rot90 = Direction(-direction.dy, direction.dx)
        rotateLeft(value - 90, rot90)
    }
}

fun calcNextPosition(
    currentPosition: Position,
    currentDir: Direction,
    moveInstruction: MoveInstruction
) = when (moveInstruction) {
    is MoveInstruction.Direct -> currentPosition.plus(
        moveInstruction.direction,
        moveInstruction.value
    ) to currentDir
    is MoveInstruction.Forward -> currentPosition.plus(currentDir, moveInstruction.value) to currentDir
    is MoveInstruction.ChangeDir -> currentPosition to rotateLeft(moveInstruction.value, currentDir)
}


fun calcNextPosition2(
    currentPosition: Position,
    currentDir: Direction,
    moveInstruction: MoveInstruction
) = when (moveInstruction) {
    is MoveInstruction.Direct -> currentPosition to (currentDir.plus(moveInstruction.direction, moveInstruction.value))
    is MoveInstruction.Forward -> currentPosition.plus(currentDir, moveInstruction.value) to currentDir
    is MoveInstruction.ChangeDir -> currentPosition to rotateLeft(moveInstruction.value, currentDir)
}
