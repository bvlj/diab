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
import xyz.diab.core.data.insulin.InsulinDataSource
import xyz.diab.core.model.Insulin
import xyz.diab.roboto.data.database.dao.InsulinDao
import xyz.diab.roboto.data.database.entity.InsulinEntity

class InsulinDataSourceImpl internal constructor(
    private val dao: InsulinDao
) : InsulinDataSource() {

    override suspend fun getAll() =
        dao.getAll()
            .map { it.toInsulin() }

    override suspend fun get(name: String): Insulin {
        val result = dao.getByName(name)
        if (result.isEmpty()) Insulin()

        return result[0].toInsulin()
    }

    override suspend fun put(insulin: Insulin) {
        dao.insert(insulin.toEntity())
    }

    override suspend fun delete(insulin: Insulin) {
        dao.delete(insulin.toEntity())
    }

    override fun getAllFlow() =
        dao.getAllFlow()
            .map { list -> list.map { it.toInsulin() } }

    private fun InsulinEntity.toInsulin() = Insulin(
        name,
        basal,
        halfUnits
    )

    private fun Insulin.toEntity() = InsulinEntity(
        name,
        basal,
        halfUnits
    )
}
