/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.extensions

import it.diab.core.time.DateTime
import it.diab.data.entities.TimeFrame
import org.junit.Assert
import org.junit.Test

class DateTimeExtTest {

    @Test
    fun asTimeFrame() {
        val date = DateTime.now

        Assert.assertEquals(
            TimeFrame.MORNING,
            date.with(DateTime.HOUR, 8).asTimeFrame()
        )
        Assert.assertEquals(
            TimeFrame.AFTERNOON,
            date.with(DateTime.HOUR, 18).asTimeFrame()
        )
        Assert.assertEquals(
            TimeFrame.NIGHT,
            date.with(DateTime.HOUR, 22).asTimeFrame()
        )
    }
}
