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

package xyz.diab.roboto.data.datasource

import kotlinx.coroutines.flow.map
import xyz.diab.core.data.glucose.GlucoseDataSource
import xyz.diab.core.model.Bolus
import xyz.diab.core.model.Glucose
import xyz.diab.roboto.data.database.dao.GlucoseDao
import xyz.diab.roboto.data.database.entity.GlucoseEntity

class GlucoseDataSourceImpl internal constructor(
    private val dao: GlucoseDao
) : GlucoseDataSource() {

    override suspend fun getAll() = dao.getAll()
        .map { it.toGlucose() }

    override suspend fun getInTimeRange(from: Long, to: Long) =
        dao.getInTimeRange(from, to)
            .map { it.toGlucose() }

    override suspend fun get(id: Long): Glucose {
        if (id < 0L) return Glucose()

        val results = dao.getById(id)
        if (results.isEmpty()) return Glucose()

        return results[0].toGlucose()
    }

    override suspend fun put(glucose: Glucose) =
        dao.insert(glucose.toEntity())

    override suspend fun delete(glucose: Glucose) {
        dao.delete(glucose.toEntity())
    }

    override fun getAllFlow() = dao.getAllFlow()
        .map { list -> list.map { it.toGlucose() } }

    private fun GlucoseEntity.toGlucose() = Glucose(
        _id,
        value,
        timeStamp,
        beforeMeal,
        eatLevel,
        bolus ?: Bolus(),
        basalBolus ?: Bolus()
    )

    private fun Glucose.toEntity() = GlucoseEntity(
        id,
        value,
        timeStamp,
        beforeMeal,
        eatLevel,
        if (bolus.name.isEmpty()) null else bolus,
        if (basalBolus.name.isEmpty()) null else basalBolus
    )
}
