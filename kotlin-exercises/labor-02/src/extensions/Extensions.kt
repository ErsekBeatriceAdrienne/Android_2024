package src.extensions

// compact functions
fun String.monogram ( ) : String =  split(" ").joinToString("") { it.first().uppercase() }

fun List < String >.joinWithSeparator( separator: String ) = joinToString(separator)
