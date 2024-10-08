package src.test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import src.extra.TextGenerator

class TextGeneratorTest {

    private lateinit var textGenerator : TextGenerator

    @BeforeEach
    fun setUp()
    {
        textGenerator = TextGenerator()
    }

    @Test
    fun testLearnWords()
    {
        val inputText = "Now is not the time for sleep, now is the time for party!"
        textGenerator.learnWords(inputText)

        // Verify that the words_map is populated correctly
        val expectedPrefixes = listOf(
            "Now is",
            "is not",
            "not the",
            "the time",
            "time for",
            "for sleep,",
            "sleep, now",
            "now is",
            "is the",
            "the time",
            "time for"
        )

        val expectedPostfixes = listOf(
            listOf("not"),
            listOf("the"),
            listOf("time"),
            listOf("for"),
            listOf("sleep,", "party!"),
            listOf("now"),
            listOf("is"),
            listOf("the"),
            listOf("time"),
            listOf("for"),
            emptyList()
        )

        // Check if the prefixes exist
        expectedPrefixes.forEachIndexed { index, prefix ->
            assertTrue(textGenerator.words_map.containsKey(prefix), "Prefix '$prefix' should be present in words_map")
            assertEquals(expectedPostfixes[index], textGenerator.words_map[prefix], "Postfixes for prefix '$prefix' do not match")
        }
    }

    @Test
    fun testGenerateText()
    {
        val inputText = "Now is not the time for sleep, now is the time for party!"
        textGenerator.learnWords(inputText)

        val generatedText = textGenerator.generateText()

        // Verify that generated text starts with the first two words
        assertTrue(generatedText.startsWith("Now is"), "Generated text should start with 'Now is'")

        // Check that the generated text is not empty
        assertFalse(generatedText.isEmpty(), "Generated text should not be empty")

        // Verify that it ends with a punctuation mark
        assertTrue(generatedText.endsWith("!") || generatedText.endsWith(".") || generatedText.endsWith("?"),
            "Generated text should end with a punctuation mark")
    }

    @Test
    fun testGenerateTextWithEmptyLearning()
    {
        // Test when no words have been learned
        val generatedText = textGenerator.generateText()
        assertEquals("", generatedText, "Generated text should be empty when no words learned")
    }

    @Test
    fun testRandomPostfixSelection()
    {
        // Add a scenario where there are multiple postfixes for a prefix
        val inputText = "Hello there, how are you? Hello there, what do you want?"
        textGenerator.learnWords(inputText)

        // Get the prefix that has multiple postfixes
        val prefix = "Hello there,"
        assertTrue(textGenerator.words_map[prefix]?.size ?: 0 > 1, "There should be multiple postfixes for '$prefix'")

        // Generate text from the learned words
        val generatedText = textGenerator.generateText()

        // Verify that it starts with the prefix
        assertTrue(generatedText.startsWith(prefix), "Generated text should start with '$prefix'")
    }
}
