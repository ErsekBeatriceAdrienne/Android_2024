package src.Quiz

class ItemController ( val itemService : ItemService )
{
    /* 1. PICKS NR OF RANDOM QUESTIONS AND PRINTS THEM WITH THE ANSWERS AND PROMPTS THE USER TO GIVE A NUMBER */
    fun quiz ( nr : Int ) : Unit
    {
        val questions : List < Item > = itemService.selectRandomItems( nr )
        var counter_correct = 0
        var counter_incorrect = 0
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

                if ( number != null && questionItem.whichIsCorrect == number)
                {
                    println(" - Correct")
                    ++counter_correct
                }
                else {
                    println(" - Incorrect.")
                    ++counter_incorrect
                }
            }
            catch (e: NumberFormatException)
            {
                println("We need an Integer number!")
            }
            println()
        }
        println()
        println(" - In total : \n\t - Correct : ${counter_correct}\n\t - Incorrect : ${counter_incorrect}\n")
    }
}