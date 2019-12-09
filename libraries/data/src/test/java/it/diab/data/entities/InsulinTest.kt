/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.entities

import it.diab.data.extensions.insulin
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class InsulinTest {

    @Test
    fun defaults() {
        insulin { uid = 12 }.run {
            assertEquals("", name)
            assertEquals(TimeFrame.EXTRA, timeFrame)
            assertFalse(isBasal)
            assertFalse(hasHalfUnits)
        }
    }

    @Test
    fun setTimeFrame() {
        val insulin = insulin { timeFrame = TimeFrame.MORNING }
        insulin.setTimeFrame(6)

        assertEquals(TimeFrame.NIGHT, insulin.timeFrame)
    }

    @Test
    fun getDisplayedString() {
        val a = insulin { name = "test" }
        assertEquals("10.5 test", a.getDisplayedString(10.5f))
        assertEquals("2 test", a.getDisplayedString(2.0f))
    }

    @Test
    fun comparison() {
        val a = insulin {
            uid = 1
            name = "test"
        }

        val b = insulin {
            uid = 2
            name = "test"
        }

        assertTrue(a == b)
        assertTrue(a.hashCode() == b.hashCode())
    }
}
