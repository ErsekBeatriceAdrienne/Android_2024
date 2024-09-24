package main

import kotlin.random.Random

///LABOR 1

fun main(args: Array<String>)
{
    /// 1. Write a main function that adds two values (immutable variables) and prints the result using a
    // String template in the following format: 2 + 3 = 5.
    val num1 = 2
    val num2 = 4

    //println("$num1 + $num2 = ${num1 + num2}")

    /// 2. Write a main function that declares an immutable list (listOf) daysOfWeek containing the
    // days of the week.

    val daysOfTheWeek = listOf <String> ("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    //  Use a for loop that iterates over the list and prints the list to the standard output.
    //println("2. a) ")
    //daysOfTheWeek.forEach{ day -> println(day)}

    // Use a list filter to print the days starting with letter ‘T’
    /*daysOfTheWeek
        .filter { it.startsWith("T") }
        .forEach{ println(it) }*/

    // Use a list filter to print the days containing the letter ‘e’
    /*daysOfTheWeek
        .filter { it.contains("e") }
        .forEach{ println(it) }*/

    // Use a list filter to print all the days of length 6 (e.g. Friday)
    /*daysOfTheWeek
        .filter { it.length == 6 }
        .forEach { println(it) }*/

    /// 3. Write a function that checks whether a number is prime or not. Write a main function that
    //prints prime numbers within a range.
    val num3 = isPrime(4)
    //println( "The number 4 is prime? = $num3 " )

    /// 4. Write an encode and a corresponding decode function that encodes and respectively
    //decodes the characters of a string. You may use any encoding strategy.

    val originalText = " Helloo Bea vagyok"
    val shift = 64

    val encodedText = encode(originalText, shift)
    //println("Encoded: $encodedText")

    val decodedText = decode(encodedText, shift)
    //println("Decoded: $decodedText")

    /// 5. Write a compact function that prints the even numbers from a list. Use a list filter!
    val listNumbers = listOf <Int> (1,2,3,4,5,6,7,8,9,10)
    //evenNumber(listNumbers)

    /// 6. The map() performs the same transformation on every list item and returns the result list.
    //Using map, perform the following operations:

    //Double the elements of a list of integers and print it
    val doubledNumbers = listNumbers.map { it * 2 }

    //println("Doubled list: $doubledNumbers")

    // Print the days of week capitalized (e.g. MONDAY for Monday)
    val daysOfTheWeekM = mutableListOf <String> ("monday", "tuesday", "wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val capitalizedDays = daysOfTheWeekM.map { it.uppercase() }
    //capitalizedDays.forEach { day -> println( day ) }

    // Print the first character of each day capitalized (e.g. m for Monday)
    val firstChars = daysOfTheWeekM.map { it.first().uppercase() }
    //firstChars.forEach { day -> println( day ) }

    // Print the length of days (number of characters, e.g. Monday → 6)
    val lengthOfDays = daysOfTheWeekM.map { it.length }
    //lengthOfDays.forEach {  day -> println(day) }

    /// 7. Mutable lists.

    // table lists.
    // Convert the daysOfWeek immutable list into a mutable one. Remove all days containing
    //the letter ‘n’, then print the mutable list. You should get this result:
    //[Tuesday, Thursday, Friday, Saturday]
    val convertedMutableList = daysOfTheWeek.toMutableList()

    // Print each element of the list in a new line together with the index of the element (convert
    //the list to list with index using the withIndex() function!). You should get the following
    //result

    /*for ((index, day) in convertedMutableList.withIndex()) {
        println("Item at $index is $day")
    }*/

    // Sort the result list alphabetically!
    convertedMutableList.sort()

    println("Sorted list: $convertedMutableList")

    // 8. Arrays.
    //  Generate an array of 10 random integers between 0 and 100, and use forEach to print
    //each element of the array in a new line.
    val randomNumbers =  Array(10) { Random.nextInt(0, 101) }
    /*randomNumbers.forEach { number ->
        println(number)
    }*/

    // Print the array sorted in ascending order!
    randomNumbers.sort()
    //randomNumbers.forEach { n -> println("$n") }

    // Check whether the array contains any even number!

}

fun isPrime ( number : Int ) : Boolean
{
    if (number <= 1) return false
    for (i in 2..number / 2)
    {
        if (number % i == 0) return false
    }
    return true
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

fun evenNumber ( list : List < Int > ) : Unit
{
    list.forEach { n -> if (n % 2 == 0) println ( n ) }
}

fun array_evenNumber ( array : Array <Int> ) : Boolean
{

}