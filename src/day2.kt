import java.io.File
import java.lang.Integer.parseInt
import java.lang.RuntimeException

data class PasswordLine(
    val start: Int,
    val end: Int,
    val letter: Char,
    val password: String
) {
    fun isValid(): Boolean {
        val letterCount = password.toCharArray().toList().filter { it == letter }.count()
        return letterCount in start..end
    }
    fun isValidOther(): Boolean {
        val firstLetter = password[start-1] == letter
        val secondLetter = password[end-1] == letter
        //xor
        return  (firstLetter || secondLetter) && !(firstLetter && secondLetter)
    }

}

fun parsePasswordLine(input: String): PasswordLine {
    val splitted = input.split(":")
    val firstSplitted = splitted[0].split(" ");
    val numbersSplitted = firstSplitted[0].split("-")
    return PasswordLine(
        start = parseInt(numbersSplitted[0]),
        end = parseInt(numbersSplitted[1]),
        letter = firstSplitted[1].toCharArray().first(),
        password = splitted[1].strip()
    )
}

fun day2() {
    val lines = File("inputs/2.txt")
        .readLines()
        .map { parsePasswordLine(it) }

    val valid = lines.filter { it.isValid() }
    println(valid.size);
    val otherValid = lines.filter { it.isValidOther() }
    println(otherValid.size);

}


