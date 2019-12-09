/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.converters

import androidx.room.TypeConverter
import it.diab.core.time.DateTime

internal class DateConverter {

    @TypeConverter
    fun toDate(value: Long?) = DateTime(value ?: 0L)

    @TypeConverter
    fun toLong(value: DateTime?) = value?.epochMillis ?: 0L
}
