import java.io.File

fun day14() {
    val lines = File("inputs/14.txt").readLines().map { parseLine(it) }
    var startState = MemState(currentMask = MemInstruction.Mask(""))
    var endState = lines.fold(startState, ::applyLine)
    println(endState.memory.values.sum())

    startState = MemState(currentMask = MemInstruction.Mask(""))
    endState = lines.fold(startState, ::applyLine2)
    println(endState.memory.values.sum())
}

private sealed class MemInstruction {
    data class Mask(val mask: String) : MemInstruction()
    data class MemAssign(val target: Long, val value: Long) : MemInstruction()
}

private data class MemState(val memory: MutableMap<Long, Long> = mutableMapOf(), val currentMask: MemInstruction.Mask)

private fun parseLine(line: String) = when {
    line.startsWith("mask =") -> MemInstruction.Mask(line.substring(7))
    line.startsWith("mem") -> {
        val addr = line.subSequence(line.indexOf("[") + 1, line.indexOf("]")).toString().toLong()
        val value = line.substring(line.indexOf("=") + 2).toLong()
        MemInstruction.MemAssign(addr, value)
    }
    else -> throw Exception("not handled $line")
}

private fun applyLine(state: MemState, line: MemInstruction) = when (line) {
    is MemInstruction.Mask -> state.copy(currentMask = line)
    is MemInstruction.MemAssign -> {
        state.memory[line.target] = applyMask(line.value, state.currentMask)
        state
    }
}

private fun applyMask(value: Long, mask: MemInstruction.Mask) =
    value.padToMask().zip(mask.mask).map { (v, m) ->
        when (m) {
            'X' -> v
            else -> m
        }
    }.joinToString("").toLong(2)

private fun applyLine2(state: MemState, line: MemInstruction) = when (line) {
    is MemInstruction.Mask -> state.copy(currentMask = line)
    is MemInstruction.MemAssign -> {
        applyMask2(value = line.target, state.currentMask).forEach { target ->
            state.memory[target] = line.value
        }
        state
    }
}

private fun applyMask2(value: Long, mask: MemInstruction.Mask): List<Long> =
    expandString("", value.padToMask().zip(mask.mask).map { (v, m) ->
        when (m) {
            '0' -> v
            '1' -> m
            'X' -> 'X'
            else -> throw Exception("invalid mask char $m")
        }
    }.joinToString("")).map { it.toLong(2) }

private fun expandString(prefix: String, value: String): List<String> = when (val firstChar = value.firstOrNull()) {
    null -> listOf(prefix)
    'X' -> expandString(prefix + "0", value.substring(1)) + expandString(prefix + "1", value.substring(1))
    '0' -> expandString(prefix + "0", value.substring(1))
    '1' -> expandString(prefix + "1", value.substring(1))
    else -> throw Exception("invalid char $firstChar")
}

private fun Long.padToMask() = toString(2).padStart(36, '0')
