/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab.roboto.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import xyz.diab.roboto.data.database.entity.GlucoseEntity

@Dao
internal interface GlucoseDao {

    @Query("SELECT * FROM glucoses ORDER BY timeStamp DESC")
    suspend fun getAll(): List<GlucoseEntity>

    @Query("SELECT * FROM glucoses WHERE timeStamp >= :from AND timeStamp <= :to ORDER BY timeStamp DESC")
    suspend fun getInTimeRange(from: Long, to: Long): List<GlucoseEntity>

    @Query("SELECT * FROM glucoses where _id == :id LIMIT 1")
    suspend fun getById(id: Long): List<GlucoseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GlucoseEntity): Long

    @Delete
    suspend fun delete(item: GlucoseEntity)

    @Query("SELECT * FROM glucoses ORDER BY timeStamp DESC")
    fun getAllFlow(): Flow<List<GlucoseEntity>>
}
