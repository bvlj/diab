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
import it.diab.data.extensions.hba1c
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class Hba1cRepositoryTest {

    private lateinit var repository: Hba1cRepository

    @Before
    fun setup() {
        AppDatabase.TEST_MODE = true

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = Hba1cRepository.getInstance(context)
    }

    @Test
    fun insert() = runBlocking {
        val item = hba1c {
            uid = 12
            value = 12.3f
        }

        repository.insert(item)
        val inRepo = repository.getById(item.uid)

        assertEquals(item, inRepo)
        assertEquals(item.uid, inRepo?.uid ?: -1)
    }

    @Test
    fun delete() = runBlocking {
        val item = hba1c {
            uid = 12
            value = 12.3f
        }

        repository.insert(item)
        assertNotNull(repository.getById(item.uid))

        repository.delete(item)
        assertNotEquals(item.uid, repository.getById(item.uid))
    }

    private suspend fun Hba1cRepository.getById(uid: Long) = getAllItems().firstOrNull { it.uid == uid }
}
