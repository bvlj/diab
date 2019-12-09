/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.insulin
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.insulin.components.status.EditableOutStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditorViewModelTest {
    private lateinit var glucoseRepository: GlucoseRepository
    private lateinit var insulinRepository: InsulinRepository
    private lateinit var viewModel: EditorViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        glucoseRepository = GlucoseRepository.getInstance(context).apply { setDebugMode() }
        insulinRepository = InsulinRepository.getInstance(context).apply { setDebugMode() }
        viewModel = EditorViewModel(glucoseRepository, insulinRepository)
    }

    @Test
    fun setInsulin() = runBlocking {
        val insulin = insulin {
            uid = 12
            name = "FooBar"
        }

        insulinRepository.insert(insulin)

        viewModel.setInsulin(insulin.uid) { result ->
            assertEquals(insulin.uid, result.uid)
            assertEquals(insulin, result)
        }
    }

    @Test
    fun delete() = runBlocking {
        val insulin = insulin {
            uid = 12
            name = "FooBar"
        }

        insulinRepository.insert(insulin)

        viewModel.runSetInsulin(insulin.uid)
        viewModel.delete(false)

        assertNotEquals(insulin.uid, insulinRepository.getById(insulin.uid).uid)
    }

    @Test
    fun save() = runBlocking {
        val origSize = insulinRepository.getInsulins().size

        viewModel.runSetInsulin(-1)
        viewModel.save(
            EditableOutStatus(
                name = "BarFoo",
                timeFrameIndex = TimeFrame.LATE_MORNING.ordinal,
                hasHalfUnits = true,
                isBasal = false
            )
        )

        assertEquals(origSize + 1, insulinRepository.getInsulins().size)
    }
}
