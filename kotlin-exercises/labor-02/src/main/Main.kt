package main
import src.date.Date
import src.date.isValid
import src.dictionary.DictionaryProvider
import src.dictionary.DictionaryType
import src.dictionary.IDictionary
import src.extensions.joinWithSeparator
import src.extensions.monogram
import src.extra.TextGenerator
import src.test.TextGeneratorTest
import java.util.Random


fun main ( )
{
    // 1. DICTIONARY
    //exercise_1()

    // 2. MONOGRAM
    // Define an extension function that prints the monogram of a String containing the
    // firstname and lastname
    exercise_2a()

    // Define a compact extension function that returns the elements of a strings’ list joined by
    // a given separator!
    exercise_2b()

    // Define a compact extension function for a strings’ list that returns the longest string!
    // Example: Longest [apple, pear, strawberry, melon] = strawberry
    exercise_2c()

    // 3. DATE
    exercise_3()

    // EXTRA
    extra_exercise()

}

fun exercise_1() : Unit
{
    val dict : IDictionary = DictionaryProvider.createDictionary( DictionaryType.TREE_SET )
    println("1. Number of words: ${dict.size()}")

    var word: String?
    println(" - Type 'exit' to go further")

    // when we write exit then exits the while cycle
    do {
        print( "  What to find? " )
        word = readLine()
        if( word.equals("quit")) break
        println("  Result: ${word?.let { dict.find(it) }}")
    }
    while ( word != "exit" )
    println()
}

fun exercise_2a() : Unit
{
    val name = "Ersek Beatrice Adrienne"
    println("2. a) Monogram : ${name.monogram()}" )
}

fun exercise_2b() : Unit
{
    val words = listOf("apple", "pear", "melon")
    println( "2. b) Joined by given separator : ${words.joinWithSeparator("#")}" )
}

fun exercise_2c() : Unit
{
    val words = listOf("apple", "pear", "strawberry", "melon")
    println("2. c) Longest: ${words.longest()}")
    println()
}

fun List < String >.longest() = maxByOrNull { it.length }

fun exercise_3() : Unit
{
    val date = Date(2024,10,1)

    val dates = mutableListOf < Date > ()
    val random = Random()

    // Generate random dates. Check the validity of the generated date. Valid dates are
    // stored in a list, while invalid ones are printed to the standard output. Repeat the
    // generation process until 10 valid dates are generated.
    println("3. a) Invalid dates : ")
    while (dates.size < 10)
    {
        val year = random.nextInt(1900, 2025)
        val month = random.nextInt(1, 13)
        val day = random.nextInt(1, 32)

        val rDate = Date( year, month, day )

        if ( rDate.isValid() ) dates.add( rDate )
        else println(" $rDate")
    }

    // Print the list. Use forEach in order to print each element to a new line.
    println("\n3. b) Valid dates : ")
    dates.forEach { println( " ${it}" ) }

    // Sort the list by defining a natural ordering for the Date class (implement the
    // Comparable<Date> interface!) Print the sorted list.
    dates.sort()

    println("\n3. c) Sorted valid dates : ")
    dates.forEach { println( " ${it}" ) }

    // Reverse the sorted list, then print it
    val reverseDates = dates.reversed()
    println("\n3. d) Reversed sorted valid dates : ")
    reverseDates.forEach { println( " ${it}" ) }

    // Sort the list by using a custom ordering (use the Comparator<Date> interface!). Print
    // the sorted list.
    val comparator = Comparator < Date >
    { date1, date2 ->
        when
        {
            date1.year != date2.year -> date1.year - date2.year
            date1.month != date2.month -> date1.month - date2.month
            else -> date1.day - date2.day
        }
    }

    dates.sortWith(comparator)
    println("\n3. e) Custom sorted valid dates (ascending order) : ")
    dates.forEach { println( " ${it}" ) }
    println()
}

fun extra_exercise ( ) : Unit
{
    println ("EXTRA EXERCISE")
    val text = "Now is not the time for sleep, now is the time for party!"
    println (" - Input : $text")

    val textGenerator = TextGenerator ( )
    textGenerator.learnWords( text )
    val generated = textGenerator.generateText ( )

    //println("\n - Generated text: $generated")

    val test = TextGeneratorTest ( )
    test.setUp()
    test.testGenerateTextWithEmptyLearning()
}