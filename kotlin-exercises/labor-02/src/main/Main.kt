package main
import src.date.Date
import src.dictionary.DictionaryProvider
import src.dictionary.DictionaryType
import src.dictionary.IDictionary
import src.dictionary.ListDictionary
import src.extensions.monogram


fun main( args: Array<String> )
{
    // 1. DICTIONARY
    val dict : IDictionary = DictionaryProvider.createDictionary( DictionaryType.TREE_SET )
    println("1. Number of words: ${dict.size()}")

    var word: String?

    // when we write exit then exits the while cycle
    do {
        print( "  What to find? " )
        word = readLine()
        if( word.equals("quit")) break
        println("  Result: ${word?.let { dict.find(it) }}")
    }
    while ( word != "exit" )
    println()

    // 2. MONOGRAM
    val name = "Ersek Beatrice Adrienne"
    println("2. Monogram : ${name.monogram()}" )

    // 3. DATE
    val date = Date(2024,10,1)

}