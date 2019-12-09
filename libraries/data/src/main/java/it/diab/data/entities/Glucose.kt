/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.entities

import androidx.annotation.IntDef
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.diab.core.time.DateTime
import it.diab.data.converters.DateConverter
import it.diab.data.converters.TimeFrameConverter

@Entity(
    tableName = "glucose",
    indices = [Index(value = ["date", "uid"], unique = true)]
)
class Glucose {

    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0

    @ColumnInfo(name = "value")
    var value: Int = 0
    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter::class)
    var date: DateTime = DateTime.now
    @ColumnInfo(name = "insulinId0")
    var insulinId0: Long = -1
    @ColumnInfo(name = "insulinValue0")
    var insulinValue0: Float = 0f
    @ColumnInfo(name = "insulinId1")
    var insulinId1: Long = -1
    @ColumnInfo(name = "insulinValue1")
    var insulinValue1: Float = 0f
    @EatLevel
    @ColumnInfo(name = "eatLevel")
    var eatLevel: Int = MEDIUM
    @ColumnInfo(name = "timeFrame")
    @TypeConverters(TimeFrameConverter::class)
    var timeFrame: TimeFrame = TimeFrame.EXTRA

    @Ignore
    constructor()

    constructor(
        uid: Long,
        value: Int,
        date: DateTime,
        insulinId0: Long,
        insulinValue0: Float,
        insulinId1: Long,
        insulinValue1: Float,
        @EatLevel eatLevel: Int,
        timeFrame: TimeFrame
    ) {
        this.uid = uid
        this.value = value
        this.date = date
        this.insulinId0 = insulinId0
        this.insulinValue0 = insulinValue0
        this.insulinId1 = insulinId1
        this.insulinValue1 = insulinValue1
        this.eatLevel = eatLevel
        this.timeFrame = timeFrame
    }

    override fun toString() = "Glucose $uid: value: $value, date: $date, " +
        "insulin0: {$insulinId0, $insulinValue0}, insulin1: {$insulinId1, $insulinValue1}, " +
        "eat: $eatLevel, timeFrame: $timeFrame"

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Glucose) {
            return false
        }

        return other.value == value &&
            other.date == date &&
            other.insulinId0 == insulinId0 &&
            other.insulinValue0 == insulinValue0 &&
            other.insulinId1 == insulinId1 &&
            other.insulinValue1 == insulinValue1 &&
            other.eatLevel == eatLevel &&
            other.timeFrame == timeFrame
    }

    override fun hashCode() = value.hashCode() or date.hashCode() or
        insulinId0.hashCode() or insulinValue0.hashCode() or
        insulinId1.hashCode() or insulinValue1.hashCode() or
        eatLevel.hashCode() or timeFrame.hashCode()

    companion object {

        @IntDef(
            LOW,
            MEDIUM,
            HIGH,
            MAX
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class EatLevel

        const val LOW = 0
        const val MEDIUM = 1
        const val HIGH = 2
        const val MAX = 3
    }
}
