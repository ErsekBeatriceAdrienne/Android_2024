package src.date

data class Date
    (
            val year : Int,
            val month : Int,
            val day : Int
) : Comparable < Date >
{
    override fun compareTo(other : Date) : Int
    {
        return when
        {
            this.year != other.year -> this.year - other.year
            this.month != other.month -> this.month - other.month
            else -> this.day - other.day
        }
    }
}

fun Date.isValid() : Boolean
{
    if (year !in 1..2024) return false
    if (month !in 1..12) return false
    val daysInMonth = when (month)
    {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (this.isLeapYear()) 29 else 28
        else -> return false
    }
    return day in 1..daysInMonth
}

fun Date.isLeapYear() : Boolean
{
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}