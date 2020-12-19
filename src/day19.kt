import java.io.File
import java.lang.RuntimeException

sealed class Rule(open val index: Int) {
    data class Simple(
        override val index: Int,
        val char: Char,
    ) : Rule(index)

    data class Options(
        override val index: Int,
        val options: List<List<Int>>,
    ) : Rule(index)
}

fun day19() {
    val (rulesText, text) = File("inputs/19_2.txt").readText().split("\n\n")
    val messages = text.split("\n")
    val isChar = "([0-9]+): \"([a-z])\"".toRegex()
    val refRules = "([0-9]+):".toRegex()
    val rules = rulesText.split("\n").map {
        //println(it)
        when (val f = isChar.find(it)) {
            null -> {
                val index = refRules.find(it)!!.groups[1]!!.value.toInt()
                val options = it.split(":")[1]!!.trim().split("|").map {
                    it.trim().split(" ").map { it.toInt() }
                }
                Rule.Options(index, options)
            }
            else -> {
                Rule.Simple(f.groups[1]!!.value.toInt(), f.groups[2]!!.value.toCharArray().first())
            }
        }
    }
    val rulesMap = rules.map { it.index to it }.toMap()
    val rulesStrings = mutableMapOf<Int, String>()

    fun resolveAndPut(rule: Rule, parents: Set<Rule>,loop: Int): String {
        if (loop > 10) {
            throw Exception("loop")
        }
        val loopAdd = when(parents.contains(rule)){
            true -> 1
            false -> 0
        }
        val entry = rulesStrings[rule.index]
        if (entry != null) {
            return entry
        }
        val res = when (rule) {
            is Rule.Simple -> rule.char.toString()
            is Rule.Options -> {
                "(" + rule.options.mapNotNull {
                    try {
                        it.map { resolveAndPut(rulesMap[it]!!, parents + setOf(rule),loop + loopAdd) }.joinToString("")
                    } catch (e: Exception) {
                        null
                    }
                }.joinToString(separator = "|") { "($it)" } + ")"

            }
        }
        rulesStrings[rule.index] = res
        return res
    }
    println(messages.filter { isValidMessage(resolveAndPut(rulesMap[0]!!, emptySet(),0).toRegex(), it) }.count())
}


fun isValidMessage(rule: Regex, msg: String) = rule.matches(msg)

