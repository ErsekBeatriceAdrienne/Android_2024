package src.dictionary

import java.io.File

// singleton ha object
object ListDictionary : IDictionary
{
    private val words : MutableList < String > = mutableListOf < String >()

    init {
        // file reading
        File(IDictionary.DICTIONARY_FILE).forEachLine { words.add(it) }
    }

    override fun add( word : String) : Boolean
    {
        if ( find(word) ) return false
        words.add(word)
        return true
    }

    override fun find( word: String ): Boolean
    {
        if( words.contains(word)) return true
        return false
    }

    override fun size(): Int {
        return words.size
    }
}