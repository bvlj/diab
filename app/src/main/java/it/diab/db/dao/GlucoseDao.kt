/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import it.diab.db.converters.DateConverter
import it.diab.db.entities.Glucose

@Dao
@TypeConverters(DateConverter::class)
interface GlucoseDao {

    @get:Query("SELECT * FROM glucose ORDER BY date DESC")
    val all: LiveData<List<Glucose>>

    @get:Query("SELECT * FROM glucose ORDER BY date DESC")
    val allStatic: List<Glucose>

    @get:Query("SELECT * FROM glucose ORDER BY date DESC")
    val pagedList: DataSource.Factory<Int, Glucose>

    @Query("SELECT * FROM glucose WHERE uid IN (:uids)")
    fun getById(vararg uids: Long): List<Glucose>

    @Query("SELECT * FROM glucose WHERE date >= :minTime AND date <= :maxTime ORDER BY date DESC")
    fun getInDateRange(minTime: Long, maxTime: Long): List<Glucose>

    @Query("SELECT * FROM glucose WHERE date >= :minTime AND date <= :maxTime AND timeFrame = :timeFrame ORDER BY date DESC")
    fun getInDateRangeWithTimeFrame(minTime: Long, maxTime: Long, timeFrame: Int): List<Glucose>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg glucose: Glucose)

    @Delete
    fun delete(glucose: Glucose)
}
