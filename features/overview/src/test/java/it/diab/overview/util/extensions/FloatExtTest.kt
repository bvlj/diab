/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.util.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FloatExtTest {

    @Test
    fun isZeroOrNan() {
        assertTrue(0f.isZeroOrNan())
        assertTrue(Float.NaN.isZeroOrNan())
        @Suppress("DIVISION_BY_ZERO")
        assertTrue((0f / 0f).isZeroOrNan())
        assertFalse(2f.isZeroOrNan())
    }
}
