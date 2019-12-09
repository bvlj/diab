/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.diab.core.time.DateTime
import it.diab.data.converters.DateConverter
import kotlin.math.roundToInt

@Entity(tableName = "hba1c")
class Hba1c {

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0

    @ColumnInfo(name = "value")
    var value: Float = 0f

    @ColumnInfo
    @TypeConverters(DateConverter::class)
    var date: DateTime = DateTime.now

    @Ignore
    constructor()

    constructor(uid: Long, value: Float, date: DateTime) {
        this.uid = uid
        this.value = value
        this.date = date
    }

    override fun toString() = "$uid: $value, $date"

    override fun equals(other: Any?) = other != null &&
        other is Hba1c &&
        other.value == value &&
        other.date == date

    override fun hashCode() = (value * 1000).roundToInt() or date.hashCode()
}
