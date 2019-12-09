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
import xyz.diab.roboto.data.database.entity.InsulinEntity

@Dao
internal interface InsulinDao {

    @Query("SELECT * FROM insulins ORDER BY name")
    suspend fun getAll(): List<InsulinEntity>

    @Query("SELECT * FROM insulins WHERE name == :name LIMIT 1")
    suspend fun getByName(name: String): List<InsulinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InsulinEntity)

    @Delete
    suspend fun delete(item: InsulinEntity)

    @Query("SELECT * FROM insulins ORDER BY name")
    fun getAllFlow(): Flow<List<InsulinEntity>>
}
