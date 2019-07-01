package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}
fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
       TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY-> value * DAY
    }
    this.time = time
    return this
}
fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = date.time - this.time
    val absDiff = abs(diff)
    val isPast = diff > 0

    return when {
        absDiff / SECOND <= 1 -> "только что"
        absDiff / SECOND <= 45 -> if (diff > 0) "несколько секунд назад" else "через несколько секунд"
        absDiff / SECOND <= 75 -> if (diff > 0) "минуту назад" else "через минуту"
        absDiff / MINUTE <= 45 -> plurals(absDiff / MINUTE, isPast, TimeUnits.MINUTE)
        absDiff / MINUTE <= 75 -> if (diff > 0) "час назад" else "через час"
        absDiff / HOUR <= 22 -> plurals(absDiff / HOUR, isPast, TimeUnits.HOUR)
        absDiff / HOUR <= 26 -> if (diff > 0) "день назад" else "через день"
        absDiff / DAY <= 360 -> plurals(absDiff / DAY, isPast, TimeUnits.DAY)
        else -> if (diff > 0) "более года назад" else "более чем через год"
    }
}

private fun plurals(diff: Long, isPast: Boolean, units: TimeUnits): String {
    val ost = diff % 10
    val cel = diff / 10
    val plurals: Map<TimeUnits, Map<String, String>> = mapOf(
        TimeUnits.MINUTE to mapOf(
            "FEW" to "минуты",
            "ONE" to "минуту",
            "MANY" to "минут"
        ),
        TimeUnits.HOUR to mapOf(
            "FEW" to "часа",
            "ONE" to "час",
            "MANY" to "часов"
        ),
        TimeUnits.DAY to mapOf(
            "FEW" to "дня",
            "ONE" to "день",
            "MANY" to "дней"
        )
    )

    return when {
        (ost in 2..4) && (cel != 1L) ->
            if (isPast) "$diff ${plurals[units]?.get("FEW")} назад"
            else "через $diff ${plurals[units]?.get("FEW")}"
        (ost == 1L) && (cel != 1L) ->
            if (isPast) "$diff ${plurals[units]?.get("ONE")} назад"
            else "через $diff ${plurals[units]?.get("ONE")}"
        else ->
            if (isPast) "$diff ${plurals[units]?.get("MANY")} назад"
            else "через $diff ${plurals[units]?.get("MANY")}"
    }
}
enum class TimeUnits{
    SECOND,
    MINUTE,
    HOUR,
    DAY
}