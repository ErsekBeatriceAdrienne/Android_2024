package src.Quiz

class ItemRepository
{
    var items : MutableList < Item > = mutableListOf (
            Item(2,"Which is the capital city of Hungary?", listOf("Bucharest", "Budapest", "Berlin")),
            Item(1,"When did the first landing on the Moon happen?", listOf("1979", "1969", "1989")),
            Item(0, "What is the capital of France?", listOf("Paris", "London", "Berlin")),
            Item(1, "Which planet is known as the Red Planet?", listOf("Mars", "Earth", "Jupiter")),
            Item(2, "What is the largest mammal?", listOf("Blue Whale", "Elephant", "Giraffe")),
            Item(3, "Who wrote 'Romeo and Juliet'?", listOf("William Shakespeare", "Jane Austen", "Mark Twain")),
            Item(4, "What is the chemical symbol for water?", listOf("H2O", "CO2", "O2")),
            Item(5, "Which continent is known as the 'Dark Continent'?", listOf("Africa", "Asia", "South America")),
            Item(6, "In which year did the Titanic sink?", listOf("1912", "1905", "1918")),
            Item(7, "What is the powerhouse of the cell?", listOf("Mitochondria", "Nucleus", "Ribosome")),
            Item(8, "Who painted the Mona Lisa?", listOf("Leonardo da Vinci", "Pablo Picasso", "Vincent van Gogh")),
            Item(9, "What is the hardest natural substance on Earth?", listOf("Diamond", "Gold", "Iron"))
    )

    /* RETURNS A RANDOM ITEM FRO THE LIST */
    fun randomItem() : Item = items.random()

    /* ADDS THE ITEM GIVEN */
    fun save ( item : Item ) : Unit { items.add(item) }

    /* RETURNS THE SIZE OF THE LIST */
    fun size() : Int = items.size

}