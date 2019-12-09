/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.util

import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.insulin
import org.junit.Assert.assertEquals
import org.junit.Test

class InsulinSelectorTest {

    @Test
    fun suggestInsulin() {
        val list = listOf(
            insulin {
                timeFrame = TimeFrame.NIGHT
                isBasal = true
            },
            insulin {
                timeFrame = TimeFrame.DINNER
            },
            insulin {
                uid = 2
                timeFrame = TimeFrame.MORNING
            }
        )

        assertEquals(1, InsulinSelector(TimeFrame.DINNER).suggestInsulin(list, -1))
        assertEquals(2, InsulinSelector(TimeFrame.MORNING).suggestInsulin(list, 2))
        assertEquals(1, InsulinSelector(TimeFrame.NIGHT).suggestInsulin(list, -1))
    }

    @Test
    fun suggestBasal() {
        val list = listOf(
            insulin {
                timeFrame = TimeFrame.NIGHT
                isBasal = false
            },
            insulin {
                timeFrame = TimeFrame.DINNER
                isBasal = true
            }
        )

        assertEquals(1, InsulinSelector(TimeFrame.DINNER).suggestBasal(list, -1))
        assertEquals(0, InsulinSelector(TimeFrame.NIGHT).suggestInsulin(list, -1))
    }
}
