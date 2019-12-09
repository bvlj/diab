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

package xyz.diab.core.data.glucose

import kotlinx.coroutines.flow.Flow
import xyz.diab.core.data.Repository
import xyz.diab.core.model.Glucose

/**
 * The basic [Repository] for the [Glucose] data type.
 * May be extended to provide more features.
 */
open class GlucoseRepository(
    private val dataSource: GlucoseDataSource
) : Repository {

    suspend fun getAll(): List<Glucose> =
        dataSource.getAll()

    suspend fun getInTimeRange(from: Long, to: Long): List<Glucose> =
        dataSource.getInTimeRange(from, to)

    suspend fun get(id: Long): Glucose =
        dataSource.get(id)

    suspend fun put(glucose: Glucose) =
        dataSource.put(glucose)

    suspend fun delete(glucose: Glucose) {
        dataSource.delete(glucose)
    }

    fun getAllFlow(): Flow<List<Glucose>> =
        dataSource.getAllFlow()
}
