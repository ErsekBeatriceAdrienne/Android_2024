package src.Quiz

class ItemService ( val repository : ItemRepository )
{
    val itemRepository : ItemRepository = ItemRepository()

    /* SELECTS RANDOM ITEM FROM THE LIST*/
    fun selectRandomItems( nr : Int ) : List < Item > = itemRepository.items.shuffled().take( nr )
}