package main

import kotlin.random.Random

/* LABOR 1 */
fun main(args: Array<String>)
{
    /// 1. Write a main function that adds two values (immutable variables) and prints the result using a
    // String template in the following format: 2 + 3 = 5.
    val num1 = 2
    val num2 = 4

    println("1. The sum of $num1 + $num2 = ${num1 + num2}\n")

    /// 2. Write a main function that declares an immutable list (listOf) daysOfWeek containing the
    // days of the week.

    val daysOfTheWeek = listOf <String> ("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    //  Use a for loop that iterates over the list and prints the list to the standard output.
    println("2. a)  Use a for loop that iterates over the list and prints the list to the standard output.")
    daysOfTheWeek.forEach{ day -> println(day)}
    println()

    // Use a list filter to print the days starting with letter ‘T’
    println("2. b) Use a list filter to print the days starting with letter ‘T’")
    daysOfTheWeek
        .filter { it.startsWith("T") }
        .forEach{ println(it) }
    println()

    // Use a list filter to print the days containing the letter ‘e’
    println("2. c) Use a list filter to print the days containing the letter ‘e’")
    daysOfTheWeek
        .filter { it.contains("e") }
        .forEach{ println(it) }
    println()

    // Use a list filter to print all the days of length 6 (e.g. Friday)
    println("2. d) Use a list filter to print all the days of length 6 (e.g. Friday)")
    daysOfTheWeek
        .filter { it.length == 6 }
        .forEach { println(it) }
    println()

    /// 3. Write a function that checks whether a number is prime or not. Write a main function that
    //prints prime numbers within a range.
    println("3. Prime number of not with function")
    val num3 = is_prime(4)
    println( "The number 4 is prime? = $num3\n" )

    /// 4. Write an encode and a corresponding decode function that encodes and respectively
    //decodes the characters of a string. You may use any encoding strategy.
    exercise_4 ()

    /// 5. Write a compact function that prints the even numbers from a list. Use a list filter!
    val listNumbers = listOf <Int> (1,2,3,4,5,6,7,8,9,10)
    exercise_5 ( listNumbers )

    /// 6. The map() performs the same transformation on every list item and returns the result list.
    //Using map, perform the following operations:
    exercise_6 ( listNumbers )

    /// 7. Mutable lists.
    // Convert the daysOfWeek immutable list into a mutable one. Remove all days containing
    //the letter ‘n’, then print the mutable list.
    val convertedMutableList = daysOfTheWeek.toMutableList()
    exercise_7 ( convertedMutableList )

    // 8. Arrays.
    exercise_8 ()

}

fun is_prime ( number : Int ) : Boolean
{
    if (number <= 1) return false
    for (i in 2..number / 2)
        if (number % i == 0) return false
        return true
}

fun exercise_4 ( ) : Unit
{
    println("4.  Write an encode and a corresponding decode function that encodes and respectively decodes text")
    val originalText = " Helloo Bea vagyok"
    val shift = 64

    // encoded
    val encodedText = encode(originalText, shift)
    println("Encoded: $encodedText")
    // decoded
    val decodedText = decode(encodedText, shift)
    println("Decoded: $decodedText\n")
}

fun encode(input: String, code: Int): String
{
    val encoded = StringBuilder()
    for (char in input)
    {
        val shifted = char + code
        encoded.append(shifted)
    }
    return encoded.toString()
}

fun decode(encoded: String, code: Int): String
{
    val decoded = StringBuilder()
    for (char in encoded)
    {
        val shifted = char - code
        decoded.append(shifted)
    }
    return decoded.toString()
}

fun messageCoding(msg: String, func: (String) -> String): String // func: (String) -> String - function
{
    return func(msg)
}

fun exercise_5 ( list : List < Int > ) : Unit
{
    println("5. Write a compact function that prints the even numbers from a list. Use a list filter!")
    even_number ( list )
}

fun even_number ( list : List < Int > ) : Unit
{
    list.forEach {
        n -> if (n % 2 == 0) println ( n )
    }
    println()
}

fun exercise_6 ( list : List < Int > ) : Unit
{
    //Double the elements of a list of integers and print it
    println("6. a) Double the elements of a list of integers and print it")
    val doubledNumbers = list.map {
        it * 2
    }
    println("Doubled list: $doubledNumbers")
    println()

    // Print the days of week capitalized (e.g. MONDAY for Monday)
    println("6. b) Print the days of week capitalized (e.g. MONDAY for Monday)")
    val daysOfTheWeekM = mutableListOf <String> ("monday", "tuesday", "wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val capitalizedDays = daysOfTheWeekM.map { it.uppercase() }
    capitalizedDays.forEach {
        day -> println( day )
    }
    println()

    // Print the first character of each day capitalized (e.g. m for Monday)
    println("6. c) Print the first character of each day capitalized (e.g. m for Monday)")
    val firstChars = daysOfTheWeekM.map {
        it.first().uppercase()
    }

    firstChars.forEach {
        day -> println( day )
    }
    println()

    // Print the length of days (number of characters, e.g. Monday → 6)
    println("6. d) Print the length of days (number of characters, e.g. Monday → 6)")
    val lengthOfDays = daysOfTheWeekM.map {
        it.length
    }

    lengthOfDays.forEach {
        day -> println(day)
    }
    println()
}

fun exercise_7 ( list : MutableList < String > ) : Unit
{
    //Remove all days containing the letter ‘n’
    println( "7. a) Remove all days containing the letter ‘n’" )
    list.removeAll {
        it.contains('n', ignoreCase = true)
    }

    list.forEach {
        it -> println(it)
    }
    println()

    // Print each element of the list in a new line together with the index of the element (convert
    //the list to list with index using the withIndex() function!). You should get the following
    //result
    println( "7. b)  Print each element of the list in a new line together with the index of the element" )
    for ( ( index, day ) in list.withIndex())
    {
        println("Item at $index is $day")
    }
    println()

    // Sort the result list alphabetically!
    println( "7. c) Sort the result list alphabetically!" )
    list.sort()
    //printing
    println("Sorted list: $list")
    println()
}

fun exercise_8 () : Unit
{
    //  Generate an array of 10 random integers between 0 and 100, and use forEach to print
    //each element of the array in a new line.
    println("8. a) Generate an array of 10 random integers between 0 and 100")
    val randomNumbers =  Array(10) { Random.nextInt(0, 101) }
    randomNumbers.forEach {
        number -> println(number)
    }
    println()

    // Print the array sorted in ascending order!
    println( "8. b)  Print the array sorted in ascending order!" )
    randomNumbers.sort()
    randomNumbers.forEach {
        n -> println("$n")
    }
    println()

    // Check whether the array contains any even number!
    println( "8. c) Check whether the array contains any even number!" )
    val hasEvenNumber = randomNumbers.any {
        it % 2 == 0
    }

    if (hasEvenNumber) println(" - The array contains at least one even number.")
    else println(" - The array does not contain any even numbers.")
    println()

    // Check whether all the numbers are even!
    println( "8. d) Check whether all the numbers are even!" )
    val areAllEven = randomNumbers.all {
        it % 2 == 0
    }

    if (areAllEven) println(" - All numbers in the array are even.")
    else println(" - Not all numbers in the array are even.")
    println()

    // Calculate the average of generated numbers and print it using forEach!
    println( "8. c) Calculate the average of generated numbers and print it using forEach!")

    val sum = randomNumbers.sum()
    val average = sum.toDouble() / randomNumbers.size

    println("- Average number in array: $average")
    println()

}