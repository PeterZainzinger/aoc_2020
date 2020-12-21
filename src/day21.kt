import java.io.File

data class Food(
    val ingridients: Set<String>,
    val allergens: Set<String>
)

private fun parseFood(input: String): Food {
    val (i, a) = input.split("(contains")
    val ingridients = i.trim().split(" ").toSet()
    val allergens = a.replace(")", "").split(",").map { it.trim() }.toSet()
    return Food(ingridients, allergens)
}

fun day21() {
    val input = File("inputs/21.txt")
        .readLines()
        .map(::parseFood)
    println(input)

    var currentInput = input.toList()

    // allergen -> ingriedent
    val result = mutableMapOf<String, String>()


    val allAllergens = input.flatMap { it.allergens }.toSet()
    val allIngriedients = input.flatMap { it.ingridients }.toSet()

    while (result.size < allAllergens.size) {
        var found = false
        val leftAllagens = currentInput.flatMap { it.allergens }.toSet()
        allAllergens.forEach { allergen ->
            //try to resolve with a given allergen
            val options = currentInput
                .filter { it.allergens.contains(allergen) }
                .map { it.ingridients }
                .fold1 { acc, item -> acc.intersect(item) } ?: emptySet()
            println("options $allergen -> $options")
            if (options.size == 1) {
                found = true
                val matchedIngridient = options.first()
                result[allergen] = matchedIngridient
                currentInput =
                    currentInput.map {
                        it.copy(
                            ingridients = it.ingridients.filterNot { it == matchedIngridient }.toSet(),
                            allergens = it.allergens.filterNot { it == allergen }.toSet()
                        )
                    }
            }
        }

        if (!found) {
            println("nothing new is found")
            break
        }
    }
    val safeFoods = allIngriedients.subtract(result.values)
    println(safeFoods)
    val res1 = safeFoods.map { food ->
        input.map {
            when (it.ingridients.contains(food)) {
                true -> 1
                false -> 0
            }
        }.sum()
    }.sum()
    println(res1)
    println(result.map { it.key to it.value }.sortedBy { it.first }.map { it.second }.joinToString(","))
}

fun <T> Collection<T>.fold1(operation: (acc: T, T) -> T): T? = when (isEmpty()) {
    true -> null
    else -> toList().subList(1, size).fold(this.first(), operation)
}