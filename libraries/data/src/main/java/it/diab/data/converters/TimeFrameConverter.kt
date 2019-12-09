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
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.toTimeFrame

internal class TimeFrameConverter {

    @TypeConverter
    fun toInt(value: TimeFrame?) = (value ?: TimeFrame.EXTRA).ordinal

    @TypeConverter
    fun toTimeFrame(value: Int?) = value?.toTimeFrame() ?: TimeFrame.EXTRA
}
