import java.io.File
import java.lang.Integer.parseInt


val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

data class Passport(val entries: Map<String, String>) {
    fun valid() = requiredFields.all { entries.containsKey(it) }

    fun validStrict(): Boolean {
        val allFields = requiredFields.all { entries.containsKey(it) }
        if (!allFields) {
            return false
        }
        val birthYear = parseInt(entries["byr"])
        if (birthYear !in 1920..2002) {
            return false
        }

        val issuesYear = parseInt(entries["iyr"])
        if (issuesYear !in 2010..2020) {
            return false
        }
        val expireYear = parseInt(entries["eyr"])
        if (expireYear !in 2020..2030) {
            return false
        }
        val heightData = entries.getValue("hgt")

        if (heightData.endsWith("cm")) {
            val hn = parseInt(heightData.subSequence(0, heightData.length - 2).toString())
            if (hn !in 150..193) {
                return false
            }
        } else if (heightData.endsWith("in")) {
            val hn = parseInt(heightData.subSequence(0, heightData.length - 2).toString())
            if (hn !in 59..76) {
                return false
            }
        } else {
            return false
        }
        val hairColor = entries["hcl"]!!
        if (!Regex("^#[a-f0-9]{6}$").matches(hairColor) || hairColor.length != 7) {
            return false
        }

        val eyeColor = entries["ecl"]

        if (!listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(eyeColor)) {
            return false
        }
        val pid = entries["pid"]!!
        if (!Regex("\\d{9}").matches(pid)) {
            return false
        } else {
            println(pid)
        }
        return true
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

