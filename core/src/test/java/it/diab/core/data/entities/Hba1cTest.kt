/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.data.entities

import it.diab.core.util.extensions.hba1c
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
class Hba1cTest {

    @Test
    fun defaults() {
        assertEquals(0f, hba1c { uid = 12 }.value)
    }

    @Test
    fun equals() {
        val a = hba1c {
            uid = 1
            value = 10f
            date = Date(0)
        }

        val b = hba1c {
            uid = 2
            value = 10f
            date = Date(0)
        }

        val c = hba1c {
            uid = 1
            value = 11f
            date = Date(0)
        }

        assertTrue(a == b)
        assertTrue(a != c)
        assertTrue(b != c)

        assertTrue(a.hashCode() == b.hashCode())
        assertTrue(a.hashCode() != c.hashCode())
        assertTrue(a.hashCode() != c.hashCode())
    }
}