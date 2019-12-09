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

import xyz.diab.core.data.insulin.InsulinRepository
import xyz.diab.core.mock.InsulinDataSourceMock
import xyz.diab.core.model.Insulin
import xyz.diab.core.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class InsulinRepositoryTest {
    private lateinit var dataSource: InsulinDataSourceMock
    private lateinit var repo: InsulinRepository

    private val expectedSize = InsulinDataSourceMock.EXPECTED_SIZE

    @BeforeTest
    fun setup() {
        dataSource = InsulinDataSourceMock()
        repo = InsulinRepository(dataSource)
    }

    @Test
    fun `getAll() should return all the items`() {
        runBlocking {
            assertEquals(expectedSize, repo.getAll().size)
        }
    }

    @Test
    fun `put() should increase the items count by 1`() {
        runBlocking {
            repo.put(Insulin(name = "D"))
            assertEquals(expectedSize + 1, repo.getAll().size)
        }
    }

    @Test
    fun `put() should not increase the items count by 1 if an item with the same id is found`() {
        runBlocking {
            repo.put(Insulin(name = "A"))
            assertEquals(expectedSize, repo.getAll().size)
        }
    }

    @Test
    fun `delete() should decrease the items count by 1`() {
        runBlocking {
            repo.delete(Insulin(name = "A"))
            assertEquals(expectedSize - 1, repo.getAll().size)
        }
    }

    @Test
    fun `delete() should not decrease the items count by 1 if the item does not exist`() {
        runBlocking {
            repo.delete(Insulin(name = "D"))
            assertEquals(expectedSize, repo.getAll().size)
        }
    }
}
