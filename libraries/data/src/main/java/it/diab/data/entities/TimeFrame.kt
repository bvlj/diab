/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.entities

import androidx.annotation.StringRes
import it.diab.data.R

enum class TimeFrame {
    EXTRA, MORNING, LATE_MORNING, LUNCH, AFTERNOON, DINNER, NIGHT, UNUSED;

    @get:StringRes
    val nameRes: Int
        get() = when (this) {
            EXTRA -> R.string.time_extra
            MORNING -> R.string.time_morning
            LATE_MORNING -> R.string.time_late_morning
            LUNCH -> R.string.time_lunch
            AFTERNOON -> R.string.time_afternoon
            DINNER -> R.string.time_dinner
            NIGHT -> R.string.time_night
            UNUSED -> R.string.time_unused
        }

    val reprHour: Int
        get() = when (this) {
            MORNING -> 6
            LATE_MORNING -> 10
            LUNCH -> 13
            AFTERNOON -> 16
            DINNER -> 19
            NIGHT -> 22
            else -> -1
        }
}
