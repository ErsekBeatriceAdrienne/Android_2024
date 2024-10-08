package src.Quiz

class ItemService ( val repository : ItemRepository )
{
    /* SELECTS RANDOM ITEM FROM THE LIST*/
    fun selectRandomItems( nr : Int ) : List < Item > = repository.items.shuffled().take( nr )
}