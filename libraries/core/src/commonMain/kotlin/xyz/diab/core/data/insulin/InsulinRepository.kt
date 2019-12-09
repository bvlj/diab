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

package xyz.diab.core.data.insulin

import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import xyz.diab.core.data.Repository
import xyz.diab.core.model.Insulin

/**
 * The basic [Repository] for the [Insulin] data type.
 * May be extended to provide more features.
 */
open class InsulinRepository(
    private val dataSource: InsulinDataSource
) : Repository {

    suspend fun getAll(): List<Insulin> =
        dataSource.getAll()

    suspend fun get(name: String): Insulin =
        dataSource.get(name)

    suspend fun put(insulin: Insulin) {
        dataSource.put(insulin)
    }

    suspend fun delete(insulin: Insulin) {
        dataSource.delete(insulin)
    }

    suspend fun nameExists(name: String): Boolean = withContext(Default) {
        getAll().any { it.name == name }
    }

    fun getAllFlow(): Flow<List<Insulin>> =
        dataSource.getAllFlow()
}
