/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.time

import java.util.Date
import org.junit.Assert
import org.junit.Test

class DateTimeTest {

    @Test
    fun plus() {
        var date = DateTime(0L)
        date += 6L.seconds()
        date += 1L.minutes()

        Assert.assertEquals(66000L, date.epochMillis)
    }

    @Test
    fun minus() {
        var date = DateTime(66000L)
        date -= 6L.seconds()
        date -= 1L.minutes()

        Assert.assertEquals(0L, date.epochMillis)
    }

    @Test
    fun minusDiff() {
        val a = DateTime(66000L)
        val b = DateTime(60000L)

        Assert.assertEquals(6000L, a - b)
    }

    @Test
    fun get() {
        val date = DateTime.now
            .with(DateTime.YEAR, 2000)
            .with(DateTime.MONTH, 1)

        Assert.assertEquals(2000, date[DateTime.YEAR])
        Assert.assertEquals(1, date[DateTime.MONTH])
    }

    @Test
    fun compareTo() {
        val date = DateTime.now
        val after = date + 1L.hours()
        val before = date - 2L.hours()

        Assert.assertTrue(date > before)
        Assert.assertTrue(date < after)
        Assert.assertTrue(after > before)
    }

    @Test
    fun getEpochMillis() {
        val now = System.currentTimeMillis()
        val date = DateTime(now)

        Assert.assertEquals(now, date.epochMillis)
    }

    @Test
    fun with() {
        val now = DateTime.now
        val date = now.with(DateTime.DAY, 3)

        Assert.assertEquals(3, date[DateTime.DAY])
        Assert.assertEquals(now[DateTime.YEAR], date[DateTime.YEAR])
        Assert.assertEquals(now[DateTime.MONTH], date[DateTime.MONTH])
    }

    @Test
    fun format() {
        val date = DateTime(2000, 1, 3)
        Assert.assertEquals("2000-02-03", date.format("yyyy-MM-dd"))
    }

    @Test
    fun isToday() {
        val today = DateTime.now
        val tomorrow = today + 24L.hours()
        val yesterday = today - 24L.hours()
        Assert.assertTrue(today.isToday())
        Assert.assertFalse(tomorrow.isToday())
        Assert.assertFalse(yesterday.isToday())
    }

    @Test
    fun asMinutes() {
        val date = DateTime.now.with(DateTime.HOUR, 10)
            .with(DateTime.MINUTE, 30)

        Assert.assertEquals(10 * 60 + 30, date.asMinutes())
    }

    @Test
    fun asJavaDate() {
        val jDate = Date(10)
        val date = DateTime(10)

        Assert.assertEquals(jDate.time, date.asJavaDate().time)
    }

    @Test
    fun getToString() {
        val date = DateTime(2000, 0, 2)
            .with(DateTime.HOUR, 12)
            .with(DateTime.MINUTE, 30)
            .with(DateTime.SECOND, 15)

        Assert.assertEquals("2000-01-02 12:30:15", date.toString())
    }
}
