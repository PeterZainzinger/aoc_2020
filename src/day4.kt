import java.io.File
import java.lang.Integer.parseInt

val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

data class Passport(val entries: Map<String, String>) {
    fun valid() = requiredFields.all { entries.containsKey(it) }
    fun validStrict() = when {
        !valid() -> false
        !requiredFields.all { entries.containsKey(it) } -> false
        parseInt(entries["byr"]) !in 1920..2002 -> false
        parseInt(entries["iyr"]) !in 2010..2020 -> false
        parseInt(entries["eyr"]) !in 2020..2030 -> false
        !Regex("^#[a-f0-9]{6}$").matches(entries["hcl"]!!) -> false
        !listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(entries["ecl"]) -> false
        !Regex("\\d{9}").matches(entries["pid"]!!) -> false
        else -> {
            val heightData = entries.getValue("hgt")
            when {
                heightData.endsWith("cm") -> {
                    val hn = parseInt(heightData.subSequence(0, heightData.length - 2).toString())
                    hn in 150..193
                }
                heightData.endsWith("in") -> {
                    val hn = parseInt(heightData.subSequence(0, heightData.length - 2).toString())
                    hn in 59..76
                }
                else -> false
            }
        }
    }
}

fun day4() {
    val lines = File("inputs/4.txt")
        .readLines().joinToString("\n").split("\n\n")
        .map { line -> line.replace("\n", " ") }
        .map { line -> line.split(" ") }
        .map { keyPairs ->
            keyPairs.map { pairData ->
                val splitted = pairData.split(":")
                splitted.first() to splitted[1]
            }.toMap()
        }
        .map { Passport(it) }
    println(lines.filter { it.valid() }.size)
    println(lines.filter { it.validStrict() }.size)
}

