import java.io.File

fun day25() {
    val (cardPublicKey, doorPublicKey) =
        File("inputs/25.txt")
            .readLines().map { it.toLong() }
    val doorLoop = searchLoopSize(doorPublicKey, 7)
    //println(doorLoop)
    val cardLoop = searchLoopSize(cardPublicKey, 7)
    //println(cardLoop)
    println(f(doorPublicKey, cardLoop))

}

fun searchLoopSize(publicKey: Long, subject: Long): Int {
    var i = 1
    var currentValue = 1L
    while (true) {
        currentValue = f_next(currentValue, subject)
        if (currentValue == publicKey) {
            break
        }
        i++
    }
    return i
}

fun f(subject: Long, loopSize: Int): Long {
    var res = 1L
    (0 until loopSize).forEach {
        res = f_next(res, subject)
    }
    return res
}

fun f_next(currentValue: Long, subject: Long): Long {
    var res = currentValue
    res *= subject
    res %= 20201227
    return res
}