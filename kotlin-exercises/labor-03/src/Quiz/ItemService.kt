package src.Quiz

class ItemService ( val repository : ItemRepository )
{
    /* SELECTS RANDOM ITEM FROM THE LIST*/
    fun selectRandomItems( nr : Int ) : List < Item >
    {
        var random = mutableListOf < Item > ()
        var randomQuestions = mutableListOf <String> ()

        while( random.size < nr)
        {
            val r = repository.randomItem()
            val q = r.question

            if (!randomQuestions.contains(q))
            {
                random.add(r)
                randomQuestions.add(q)
            }
        }

        return random
    }
}