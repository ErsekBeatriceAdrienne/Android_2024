package src.Quiz

class ItemController ( itemService : ItemService )
{
    val itemService : ItemService = itemService

    /* 1. PICKS NR OF RANDOM QUESTIONS AND PRINTS THEM WITH THE ANSWERS AND PROMPTS THE USER TO GIVE A NUMBER */
    fun quiz ( nr : Int ) : Unit
    {
        val questions : List < Item > = itemService.selectRandomItems( nr )

        questions.forEach { questionItem ->
            println ( questionItem.question )

            questionItem.answers.forEachIndexed { index, answer ->
                println( " - ${index + 1}. $answer" )
            }

            print( "Please give the number of the answer : " )
            val choice = readlnOrNull()

            try
            {
                var number = choice?.toInt()

                if ( number != null && questionItem.whichIsCorrect == number) println(" - Correct")
                else println(" - Incorrect.")
            }
            catch (e: NumberFormatException)
            {
                println("We need an Integer number!")
            }

            println()
        }
    }
}