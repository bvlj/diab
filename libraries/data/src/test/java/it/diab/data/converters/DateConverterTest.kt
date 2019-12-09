/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.converters

import it.diab.core.time.DateTime
import org.junit.Assert
import org.junit.Test

class DateConverterTest {
    private val converter = DateConverter()

    @Test
    fun convertToDate() {
        val now = System.currentTimeMillis()
        Assert.assertEquals(now, converter.toDate(now).epochMillis)
        Assert.assertEquals(0, converter.toDate(null).epochMillis)
    }

    @Test
    fun convertToLong() {
        val now = System.currentTimeMillis()
        val date = DateTime(now)
        Assert.assertEquals(now, converter.toLong(date))
        Assert.assertEquals(0, converter.toLong(null))
    }
}
