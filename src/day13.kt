import java.io.File

sealed class ScheduleItem {
    object Placeholder : ScheduleItem()
    data class Bus(val loopTime: Int)
}


fun day13() {
    val lines = File("inputs/13.txt").readLines()
    val arrival = lines.first().toInt()
    val schedule = lines[1].split(",").map {
        when (it) {
            "x" -> ScheduleItem.Placeholder
            else -> ScheduleItem.Bus(it.toInt())
        }
    }
    val scheduleWithHoles = schedule.map {
        when (it) {
            is ScheduleItem.Bus -> it.loopTime
            else -> null
        }
    }
    val scheduleTimes = scheduleWithHoles.filterNotNull()

    val (busId, offset) = scheduleTimes
        .map { interval ->
            interval to interval - (arrival % interval)
        }
        .minBy { it.second }!!
    println(busId * offset)

    val timesWithOffsets = scheduleWithHoles.mapIndexed { index, i ->
        when (i) {
            null -> null
            else -> i to index
        }
    }.filterNotNull()
    // PART 2
    // https://de.wikipedia.org/wiki/Chinesischer_Restsatz ftw!
    val M = timesWithOffsets.fold(1L, { acc, new -> lcm(acc, (new.first).toLong()) })
    val res = timesWithOffsets.map { (m_i, a_i) ->
        val M_i = M / m_i
        val (_, r_i, s_i) = gcdExt(m_i.toLong(), M_i)
        val e_i = s_i * M_i
        a_i.toLong() * e_i
    }.sum()
    var resmod = res
    if (res < 0) {
        while (resmod + M < 0) {
            resmod += M
        }
    } else {
        while (resmod > 0) {
            resmod -= M
        }
    }
    println(-resmod)
}


fun gcd(a: Long, b: Long): Long = when (b) {
    0L -> a
    else -> gcd(b, a % b)
}

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

fun gcdExt(p: Long, q: Long): Triple<Long, Long, Long> {
    if (q == 0L) return Triple(p, 1, 0)
    val (c1, c2, c3) = gcdExt(q, p % q)
    return Triple(c1, c3, c2 - p / q * c3)
}