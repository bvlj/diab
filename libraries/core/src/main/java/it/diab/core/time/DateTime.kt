/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.time

import androidx.annotation.IntDef
import java.util.Calendar
import java.util.Date

class DateTime {
    private val calendar = Calendar.getInstance()

    private constructor()

    constructor(timeMillis: Long) {
        calendar.timeInMillis = timeMillis
    }

    constructor(year: Int, month: Int, day: Int) {
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day
    }

    operator fun plus(time: ChronoUnit): DateTime =
        DateTime(epochMillis + time.toMillis())

    operator fun minus(time: ChronoUnit): DateTime =
        DateTime(epochMillis - time.toMillis())

    operator fun minus(other: DateTime): Long =
        epochMillis - other.epochMillis

    operator fun get(@DateTimeField field: Int): Int =
        calendar[field]

    operator fun compareTo(other: DateTime): Int =
        calendar.timeInMillis.compareTo(other.calendar.timeInMillis)

    val epochMillis: Long
        get() = calendar.timeInMillis

    fun with(@DateTimeField field: Int, value: Int): DateTime = apply {
        calendar[field] = value
    }

    fun format(pattern: String): String =
        DateTimeFormatter(pattern).format(this)

    fun isToday(): Boolean {
        val today = now
        return today[YEAR] == get(YEAR) && today[DAY_OF_YEAR] == get(DAY_OF_YEAR)
    }

    fun asMinutes(): Long =
        get(HOUR) * 60L + get(MINUTE)

    internal fun asJavaDate(): Date =
        calendar.time

    override fun equals(other: Any?): Boolean =
        other is DateTime && other.epochMillis == epochMillis

    override fun hashCode(): Int =
        calendar.hashCode()

    override fun toString(): String =
        format("yyyy-MM-dd HH:mm:ss")

    companion object {

        val now: DateTime
            get() = DateTime()

        // Date getters
        @IntDef(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, DAY_OF_YEAR)
        @Retention(AnnotationRetention.SOURCE)
        annotation class DateTimeField

        const val YEAR = Calendar.YEAR
        const val MONTH = Calendar.MONTH
        const val DAY = Calendar.DAY_OF_MONTH
        const val HOUR = Calendar.HOUR_OF_DAY
        const val MINUTE = Calendar.MINUTE
        const val SECOND = Calendar.SECOND
        const val DAY_OF_YEAR = Calendar.DAY_OF_YEAR
    }
}
