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

fun DateTime.asTimeFrame() = when (get(DateTime.HOUR)) {
    in 6..9 -> TimeFrame.MORNING
    in 10..11 -> TimeFrame.LATE_MORNING
    in 12..13 -> TimeFrame.LUNCH
    in 14..18 -> TimeFrame.AFTERNOON
    in 19..20 -> TimeFrame.DINNER
    else -> TimeFrame.NIGHT
}
