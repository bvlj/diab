/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import it.diab.data.converters.TimeFrameConverter
import it.diab.data.entities.Insulin

@Dao
@TypeConverters(TimeFrameConverter::class)
internal interface InsulinDao {

    @get:Query("SELECT * FROM insulin ORDER BY timeFrame")
    val all: DataSource.Factory<Int, Insulin>

    @Query("SELECT * FROM insulin")
    suspend fun getInsulins(): List<Insulin>

    @Query("SELECT * FROM insulin WHERE isBasal = 1")
    suspend fun getBasals(): List<Insulin>

    @Query("SELECT * FROM insulin WHERE uid IN (:uids)")
    suspend fun getById(vararg uids: Long): List<Insulin>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg insulins: Insulin): Array<Long>

    @Delete
    suspend fun delete(insulin: Insulin)
}
