/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.entities

import it.diab.core.time.DateTime
import it.diab.data.extensions.glucose
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GlucoseTest {

    @Test
    fun defaults() {
        glucose { uid = 12 }.run {
            assertEquals(0, value)
            assertEquals(-1, insulinId0)
            assertEquals(0f, insulinValue0)
            assertEquals(-1, insulinId1)
            assertEquals(0f, insulinValue1)
            assertEquals(Glucose.MEDIUM, eatLevel)
            assertEquals(TimeFrame.EXTRA, timeFrame)
        }
    }

    @Test
    fun comparison() {
        val a = glucose {
            uid = 1
            value = 100
            date = DateTime(0)
        }

        val b = glucose {
            uid = 2
            value = 100
            date = DateTime(0)
        }

        assertTrue(a == b)
        assertTrue(a.hashCode() == b.hashCode())
    }
}
