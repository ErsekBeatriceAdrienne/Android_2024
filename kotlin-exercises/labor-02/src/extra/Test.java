package src.extra;

public class Test
{

    fun testGenerateTextWithNoInput()
    {
        val generatedText = generateText()
        assertEquals("", generatedText)
    }


    fun testGenerateTextWithSingleWord( inputText : String)
    {
        learnWords(inputText)
        val generatedText = generateText()

        assertEquals("", generatedText)
    }
}
