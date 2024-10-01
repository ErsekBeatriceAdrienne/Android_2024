package src.extensions

fun String.monogram() : String
{
    return split(" ").joinToString("") { it.first().uppercase() }
}

fun List<String>.joinWithSeparator (separator : String) : String
{
    return this.joinToString(separator)
}