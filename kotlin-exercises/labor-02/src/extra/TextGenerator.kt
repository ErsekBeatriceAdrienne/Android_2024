package src.extra

import kotlin.random.Random

class TextGenerator
{
    private val words_map = mutableMapOf < String, MutableList < String >> ()

    fun learnWords(text : String) : Unit
    {
        val words = text.split(" ").filter { it.isNotEmpty() }

        for (i in 0 until words.size - 2)
        {
            val prefix = "${words[i]} ${words[i + 1]}"
            val postfix = words[i + 2]

            words_map.computeIfAbsent(prefix) { mutableListOf() }.add(postfix)
        }

        if (words.size >= 2)
        {
            val lastPrefix = "${words[words.size - 2]} ${words[words.size - 1]}"
            words_map.putIfAbsent(lastPrefix, mutableListOf())
        }

        println(" - The prefixes and postfixes")
        words_map.forEach { (prefix, postfixes) -> println("\t$prefix\t- $postfixes") }
    }

    fun generateText() : String
    {
        println("\n - Output : ")
        if (words_map.isEmpty()) return ""

        var first_two_words = words_map.keys.first()
        var counter = 1
        println(" $counter. $first_two_words")

        val generated = StringBuilder(first_two_words)
        var sign : Char = first_two_words.last()

        while (sign != '!' && sign != '.' && sign != '?')
        {
            val postfix = words_map[first_two_words] ?: break
            if (postfix.isEmpty()) break
            val last_postfix = postfix.last()
            generated.append(" ").append(last_postfix)

            ++counter
            println(" $counter. $generated")

            sign = last_postfix.last()

            val prefix = first_two_words.split(" ")
            first_two_words = "${prefix[1]} $last_postfix"

            if (!words_map.containsKey(first_two_words)) break
        }
        return generated.toString()
    }

}