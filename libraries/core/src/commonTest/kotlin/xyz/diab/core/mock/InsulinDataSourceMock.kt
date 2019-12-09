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

package xyz.diab.core.mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import xyz.diab.core.data.insulin.InsulinDataSource
import xyz.diab.core.model.Insulin

class InsulinDataSourceMock : InsulinDataSource() {
    private val list = mutableListOf(
        Insulin("A", basal = false, halfUnits = true),
        Insulin("B", basal = false, halfUnits = false),
        Insulin("C", basal = true, halfUnits = false)
    )

    override suspend fun getAll(): List<Insulin> {
        return list
    }

    override suspend fun get(name: String): Insulin {
        return list.firstOrNull { it.name == name } ?: Insulin()
    }

    override suspend fun put(insulin: Insulin) {
        list.removeAll { it.name == insulin.name }
        list.add(insulin)
        list.sortBy { it.name }
    }

    override suspend fun delete(insulin: Insulin) {
        list.removeAll { it.name == insulin.name }
    }

    override fun getAllFlow(): Flow<List<Insulin>> {
        return flowOf(list)
    }

    companion object {
        const val EXPECTED_SIZE = 3
    }
}
