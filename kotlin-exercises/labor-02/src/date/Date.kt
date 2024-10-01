package src.date

data class Date
    (
            val year : Int,
            val month : Int,
            val day : Int
) /*: Comparable < Date >
{
    override fun compareTo(other: Date): Int {}
}*/

/*fun Date.isValidDate() : Boolean {
    if (year !in 1..2024) return false
    if (month !in 1..12) return false
    val dayOk = return when (month)
    {
        4, 6, 9, 11 -> day in 1..30
        //2 -> day in 1..nrOfDaysFebruary

    }
}
*/
fun Date.isLeapYear() : Boolean
{
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}