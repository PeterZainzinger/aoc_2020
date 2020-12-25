import java.io.File
import java.lang.RuntimeException

data class TicketClass(val name: String, val options: List<IntRange>) {
    fun containsNumber(value: Int) = options.any { it.contains(value) }
    override fun toString() = name
}

data class Ticket(val numbers: List<Int>)

private fun parseTicket(input: String) = input.split("\n").mapIndexed { index, s ->
    when {
        index == 0 || s == "" -> null
        else -> Ticket(s.split(",").map { it.toInt() })
    }
}.filterNotNull()


fun day16() {
    val input = File("inputs/16_a.txt").readText().split("\n\n")
    val ticketOptions = input.first().split("\n").map { it.split(":") }
        .map {
            TicketClass(it.first(), it[1].split(" or ").map {
                val range = it.split("-").map { it.trim().toInt() }
                range[0]..range[1]
            })
        }
    val myTicket = parseTicket(input[1]).first()
    val nearbyTickets = parseTicket(input[2])
    val validTickets = nearbyTickets.filter { validTicket(it, ticketOptions) }
    val matched = matchOptions(ticketOptions, validTickets)
    println(task1(nearbyTickets, ticketOptions))
    println(matched.filter { it.key.name.startsWith("departure") }
        .map { myTicket.numbers[it.value] }.map { it.toLong() }.fold(1L, { a, b -> a * b }))
}

fun matchOptions(options: List<TicketClass>, tickets: List<Ticket>): Map<TicketClass, Int> {
    val positionToOptions = (options.indices).map { index -> index to options }.toMap().toMutableMap()
    tickets.forEach { ticket ->
        ticket.numbers.forEachIndexed { index, i ->
            positionToOptions[index] = positionToOptions[index]!!.filter { it.containsNumber(i) }
        }
    }
    while (!positionToOptions.values.map { it.size }.all { it == 1 }) {
        val before = positionToOptions.toMap()
        before.forEach { (i, value) ->
            if (value.size == 1) {
                val toRemove = value.first()
                before.forEach { (j, other) ->
                    if (i != j) {
                        positionToOptions[j] = positionToOptions[j]!!.filter { it.name != toRemove.name }
                    }
                }
            }
        }
    }
    return positionToOptions.map { (key, value) -> value.first() to key }.toMap()
}

fun task1(nearby: List<Ticket>, classes: List<TicketClass>) = nearby.map { ticket ->
    ticket.numbers.filterNot { number -> classes.any { option -> option.containsNumber(number) } }.sum()
}.sum()

fun validTicket(ticket: Ticket, classes: List<TicketClass>): Boolean = ticket.numbers.all { number ->
    classes.any { option -> option.containsNumber(number) }
}
