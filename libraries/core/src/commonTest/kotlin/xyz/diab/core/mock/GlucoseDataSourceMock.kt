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
import xyz.diab.core.data.glucose.GlucoseDataSource
import xyz.diab.core.model.Bolus
import xyz.diab.core.model.Glucose

class GlucoseDataSourceMock : GlucoseDataSource() {
    private val list = mutableListOf(
        Glucose(6, 65f, 1500, false, 1, Bolus(), Bolus()),
        Glucose(5, 224f, 1400, false, 1, Bolus(), Bolus()),
        Glucose(4, 139f, 1300, true, 1, Bolus("B", 0.5f), Bolus("C", 4f)),
        Glucose(3, 154f, 1200, false, 1, Bolus(), Bolus()),
        Glucose(2, 90f, 1100, false, 1, Bolus(), Bolus()),
        Glucose(1, 123f, 1000, true, 1, Bolus("A", 2.1f), Bolus())
    )

    override suspend fun getAll(): List<Glucose> {
        return list
    }

    override suspend fun getInTimeRange(from: Long, to: Long): List<Glucose> {
        return list.filter { it.timeStamp in from..to }
    }

    override suspend fun get(id: Long): Glucose {
        return list.firstOrNull { it.id == id } ?: Glucose()
    }

    override suspend fun put(glucose: Glucose): Long {
        list.removeAll { it.id == glucose.id }
        list.add(glucose)
        list.sortByDescending { it.id }

        return glucose.id
    }

    override suspend fun delete(glucose: Glucose) {
        list.removeAll { it.id == glucose.id }
    }

    override fun getAllFlow(): Flow<List<Glucose>> {
        return flowOf(list)
    }

    object TestData {
        const val EXPECTED_SIZE = 6
        const val EXPECTED_SIZE_IN_RANGE = 3
    }
}
