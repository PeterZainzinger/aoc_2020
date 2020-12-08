import java.io.File
import java.lang.Integer.parseInt

sealed class Instruction {
    data class NoOp(val value: Int) : Instruction()
    data class Acc(val value: Int) : Instruction()
    data class Jump(val jumpToLine: Int) : Instruction()
}

data class ProgramState(
    val acc: Int = 0,
    val currentLine: Int = 0,
    val visitedLines: Set<Int> = emptySet(),
    val result: ProgramResult? = null
)

enum class ProgramResult {
    loop, done
}

fun parseInstruction(line: String) = when (val start = line.substring(0..2)) {
    "nop" -> Instruction.NoOp(parseInt(line.substring(4 until line.length)))
    "acc" -> Instruction.Acc(parseInt(line.substring(4 until line.length)))
    "jmp" -> Instruction.Jump(parseInt(line.substring(4 until line.length)))
    else -> throw Exception("start $start cant be used")
}

fun day8() {
    val input = File("inputs/8.txt").readLines().map(::parseInstruction)
    println(execute(input, ProgramState()).acc)
    val altPrograms = input.mapIndexed { index, instruction ->
        when (instruction) {
            is Instruction.Jump -> input.toMutableList().apply { set(index, Instruction.NoOp(instruction.jumpToLine)) }
            is Instruction.NoOp -> input.toMutableList().apply { set(index, Instruction.Jump(instruction.value)) }
            else -> null
        }
    }.filterNotNull()
    val targetProgram = altPrograms.first { execute(it, ProgramState()).result == ProgramResult.done }
    println(execute(targetProgram, ProgramState()).acc)
}


fun execute(program: List<Instruction>, state: ProgramState): ProgramState {
    if (state.currentLine in state.visitedLines) {
        return state.copy(result = ProgramResult.loop)
    }
    val nextState = when (val current = program[state.currentLine]) {
        is Instruction.NoOp -> state.copy(currentLine = state.currentLine + 1)
        is Instruction.Acc -> state.copy(acc = state.acc + current.value, currentLine = state.currentLine + 1)
        is Instruction.Jump -> state.copy(currentLine = state.currentLine + current.jumpToLine)
    }.copy(
        visitedLines = state.visitedLines + setOf(state.currentLine)
    )
    return when {
        nextState.currentLine >= program.size -> nextState.copy(result = ProgramResult.done)
        else -> execute(program, nextState)
    }
}
