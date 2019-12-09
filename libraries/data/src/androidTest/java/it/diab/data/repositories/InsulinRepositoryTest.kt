/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.repositories

import androidx.test.platform.app.InstrumentationRegistry
import it.diab.data.AppDatabase
import it.diab.data.extensions.insulin
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class InsulinRepositoryTest {

    private lateinit var repository: InsulinRepository

    @Before
    fun setup() {
        AppDatabase.TEST_MODE = true

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = InsulinRepository.getInstance(context)
    }

    @Test
    fun getBasals() = runBlocking {
        // Clear old test data
        repository.getInsulins().forEach { repository.delete(it) }

        val data = Array(6) {
            insulin {
                isBasal = it % 2 == 0
                name = "Insulin $it"
            }
        }

        data.forEach { repository.insert(it) }

        assertEquals(data.size / 2, repository.getBasals().size)
    }

    @Test
    fun insert() = runBlocking {
        val insulin = insulin {
            uid = 12
            name = "FooBar"
        }

        repository.insert(insulin)
        repository.getById(insulin.uid).run {
            assertEquals(insulin.uid, uid)
            assertEquals(insulin, this)
        }
    }

    @Test
    fun delete() = runBlocking {
        val insulin = insulin {
            uid = 12
            name = "FooBar"
        }

        repository.insert(insulin)
        assertEquals(insulin.uid, repository.getById(insulin.uid).uid)

        repository.delete(insulin)
        assertNotEquals(insulin.uid, repository.getById(insulin.uid).uid)
    }
}
