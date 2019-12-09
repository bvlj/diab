/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.extensions

import it.diab.data.entities.TimeFrame

fun Int.toTimeFrame() = when (this) {
    1 -> TimeFrame.MORNING
    2 -> TimeFrame.LATE_MORNING
    3 -> TimeFrame.LUNCH
    4 -> TimeFrame.AFTERNOON
    5 -> TimeFrame.DINNER
    6 -> TimeFrame.NIGHT
    7 -> TimeFrame.UNUSED
    else -> TimeFrame.EXTRA
}
