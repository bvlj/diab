/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import it.diab.data.converters.DateConverter
import it.diab.data.entities.Hba1c

@Dao
@TypeConverters(DateConverter::class)
internal interface Hba1cDao {

    @get:Query("SELECT * FROM hba1c ORDER BY date DESC")
    val all: LiveData<List<Hba1c>>

    @Query("SELECT * FROM hba1c ORDER BY date DESC")
    suspend fun getAllItems(): List<Hba1c>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg items: Hba1c)

    @Delete
    suspend fun delete(item: Hba1c)
}
