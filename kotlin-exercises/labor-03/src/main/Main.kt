package main

import src.Quiz.ItemController
import src.Quiz.ItemRepository
import src.Quiz.ItemService

fun main ( )
{
    val repository  :    ItemRepository     = ItemRepository()
    val service     :    ItemService        = ItemService(repository)
    val controller  :    ItemController     = ItemController(service)

    println( " - Please enter the number of questions you want to receive : " )
    val nrOfQuestionsNeeded = readlnOrNull()

    try {
        var nr = nrOfQuestionsNeeded?.toInt()
        if (nr == null) println( "This is not a valid number!" )
        else
        {
            while (nr != null && nr > repository.size())
            {
                println("The number of available questions (${repository.size()}). Please try again : ")
                nr = readlnOrNull()?.toIntOrNull()

                if (nr == null) println("This is not a valid number! Please enter a valid number.")
            }
            if ( nr != null ) controller.quiz( nr )
        }
    }
    catch ( e : Exception )
    {
        throw Exception(" This is not a number!")
    }
}