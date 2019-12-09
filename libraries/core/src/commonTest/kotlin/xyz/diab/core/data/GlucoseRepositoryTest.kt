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

package xyz.diab.core.data

import xyz.diab.core.data.glucose.GlucoseDataSource
import xyz.diab.core.data.glucose.GlucoseRepository
import xyz.diab.core.mock.GlucoseDataSourceMock
import xyz.diab.core.model.Glucose
import xyz.diab.core.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GlucoseRepositoryTest {
    private lateinit var dataSource: GlucoseDataSource
    private lateinit var repo: GlucoseRepository

    private val expectedSize = GlucoseDataSourceMock.TestData.EXPECTED_SIZE
    private val expectedInRangeSize = GlucoseDataSourceMock.TestData.EXPECTED_SIZE_IN_RANGE

    @BeforeTest
    fun setup() {
        dataSource = GlucoseDataSourceMock()
        repo = GlucoseRepository(dataSource)
    }

    @Test
    fun `getAll() should return all the items`() {
        runBlocking {
            assertEquals(expectedSize, repo.getAll().size)
        }
    }

    @Test
    fun `getInRange() should return the correct number of items`() {
        runBlocking {
            assertEquals(expectedInRangeSize, repo.getInTimeRange(1200, 1400).size)
        }
    }

    @Test
    fun `put() should increase the items count by 1`() {
        runBlocking {
            repo.put(Glucose(id = 10))
            assertEquals(expectedSize + 1, repo.getAll().size)
        }
    }

    @Test
    fun `put() should not increase the items count by 1 if an item with the same id is found`() {
        runBlocking {
            repo.put(Glucose(id = 2))
            assertEquals(expectedSize, repo.getAll().size)
        }
    }

    @Test
    fun `delete() should decrease the items count by 1`() {
        runBlocking {
            repo.delete(Glucose(id = 2))
            assertEquals(expectedSize - 1, repo.getAll().size)
        }
    }

    @Test
    fun `delete() should not decrease the items count by 1 if the item does not exist`() {
        runBlocking {
            repo.delete(Glucose(id = 10))
            assertEquals(expectedSize, repo.getAll().size)
        }
    }
}
