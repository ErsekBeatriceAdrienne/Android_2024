package src.dictionary

interface IDictionary
{
    companion object {
        const val DICTIONARY_FILE = "D:\\Sapientia\\III. Év - 2024-25\\I. Félév\\Android\\Android_2024\\kotlin-exercises\\labor-02\\src\\main\\dictionary.txt"
    }

    fun add( word : String ) : Boolean
    fun find( word : String ) :  Boolean
    fun size() : Int
}