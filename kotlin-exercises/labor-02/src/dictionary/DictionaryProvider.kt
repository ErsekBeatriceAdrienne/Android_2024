package src.dictionary

class DictionaryProvider
{
    companion object
    {
        fun createDictionary( type : DictionaryType ): IDictionary
        {
            return when (type) {
                DictionaryType.ARRAY_LIST -> ListDictionary
                DictionaryType.HASH_SET -> HashSetDictionary
                DictionaryType.TREE_SET -> TreeSetDictionary
            }
        }
    }
}