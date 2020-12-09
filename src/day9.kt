import java.io.File
import java.lang.Long.parseLong

fun day9() {
    val inputTest = File("inputs/9_test.txt").readLines().map(::parseLong)
    println(findNonMatching(inputTest, 5, 5))
    val input = File("inputs/9.txt").readLines().map(::parseLong)
    val target2 = findNonMatching(input, 25, 25).first()
    println(target2)
    val cs = findContinousSum(target2, input)!!
    println(cs.minOrNull()!! + cs.maxOrNull()!!)
}

private fun findNonMatching(input: List<Long>, currentIndex: Int, windowSize: Int): Set<Long> {
    if (currentIndex >= input.size - 1) {
        return emptySet()
    }
    val target = input[currentIndex]
    return when (canBePresentedAsSum(target, input.subList(currentIndex - windowSize, currentIndex))) {
        false -> setOf(target)
        true -> emptySet()
    } + findNonMatching(input, currentIndex + 1, windowSize)
}

private fun canBePresentedAsSum(target: Long, options: List<Long>): Boolean {
    for (i in options) {
        for (j in options) {
            if (i != j && i + j == target) {
                return true
            }
        }
    }
    return false
}

private fun findContinousSum(target: Long, options: List<Long>): List<Long>? {
    for (pivot in 0..options.size) {
        for (i in 0..options.size - pivot) {
            val subList = options.subList(pivot, pivot + i)
            val subSum = subList.sum()
            if (subSum == target) {
                return subList
            }
            if (subSum > target) {
                break
            }
        }
    }
    return null
}
