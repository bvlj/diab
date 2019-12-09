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
import xyz.diab.core.data.DataSource
import xyz.diab.core.model.Glucose

/**
 * Generic [Glucose] data source
 */
abstract class GlucoseDataSource : DataSource {

    /**
     * Get all the [Glucose] entries.
     */
    abstract suspend fun getAll(): List<Glucose>

    /**
     * Get all the [Glucose] entries in a specific
     * time range.
     *
     * @param from "From" timestamp
     * @param to "To" timestamp
     */
    abstract suspend fun getInTimeRange(from: Long, to: Long): List<Glucose>

    /**
     * Get / fetch the [Glucose] with the given id.
     *
     * @return the desired [Glucose] or a new empty one if nothing matched the given id
     */
    abstract suspend fun get(id: Long): Glucose

    /**
     * Put / insert / save the given [Glucose] entry.
     */
    abstract suspend fun put(glucose: Glucose): Long

    /**
     * Delete / remove the given [Glucose] entry.
     */
    abstract suspend fun delete(glucose: Glucose)

    /**
     * Get all the [Glucose] entries as a [Flow]
     */
    abstract fun getAllFlow(): Flow<List<Glucose>>
}
