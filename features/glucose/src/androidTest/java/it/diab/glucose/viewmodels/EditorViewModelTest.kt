/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import it.diab.data.entities.Glucose
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.glucose
import it.diab.data.extensions.insulin
import it.diab.data.plugin.PluginManager
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.glucose.components.status.EditableOutStatus
import kotlin.math.absoluteValue
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EditorViewModelTest {

    private lateinit var glucoseRepo: GlucoseRepository
    private lateinit var viewModel: EditorViewModel
    private lateinit var pluginManager: PluginManager

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testGlucose = glucose {
        uid = 15
        value = 99
        insulinValue1 = 3.5f
        eatLevel = Glucose.LOW
        timeFrame = TimeFrame.DINNER
    }
    private val testInsulin = insulin {
        uid = 1
        name = "FooBar"
        timeFrame = TimeFrame.DINNER
        hasHalfUnits = true
    }
    private val testBasal = insulin {
        uid = 2
        name = "BarFoo"
        isBasal = true
    }

    private lateinit var lifecycle: LifecycleOwner

    @Before
    fun setup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        viewModel = EditorViewModel(
            GlucoseRepository.getInstance(context),
            InsulinRepository.getInstance(context)
        )
        pluginManager = PluginManager(context)
        glucoseRepo = GlucoseRepository.getInstance(context).apply {
            setDebugMode()
            insert(testGlucose)
        }

        InsulinRepository.getInstance(context).apply {
            setDebugMode()
            insert(testInsulin)
            insert(testBasal)
        }

        val lifecycleRegistry = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycle = LifecycleOwner { lifecycleRegistry }

        // setup is required to be "void"
        Unit
    }

    @Test
    fun setGlucose() = runBlocking {
        viewModel.runPrepare(testGlucose.uid, pluginManager)
        viewModel.glucose.observe(lifecycle, Observer {
            assertEquals(testGlucose.value, viewModel.value)
            assertEquals(testGlucose.date, viewModel.date)

            assertEquals(testGlucose.value, it.value)
            // Account for time diff due to save operations
            assertTrue((testGlucose.date - it.date).absoluteValue < 1000)
        })
    }

    @Test
    fun save() = runBlocking {
        val initialSize = glucoseRepo.getInDateRange(0, System.currentTimeMillis()).size

        viewModel.runPrepare(-1, pluginManager)

        viewModel.glucose.observe(lifecycle, blockingObserver {
            viewModel.runSave(
                EditableOutStatus(
                    71,
                    Glucose.MEDIUM
                )
            )

            val finalSize = glucoseRepo.getInDateRange(0, System.currentTimeMillis()).size
            assertTrue(finalSize > initialSize)

            // Stop observing or we end up looping forever
            viewModel.glucose.removeObservers(lifecycle)
        })
    }

    @Test
    fun getInsulin() = runBlocking {
        viewModel.runPrepare(-1L, pluginManager)

        viewModel.getInsulin(testInsulin.uid).run {
            assertEquals(testInsulin.uid, uid)
            assertEquals(testInsulin, this)
        }
    }

    @Test
    fun hasPotentialBasal() = runBlocking {
        val glucose = glucose {
            uid = 71
            timeFrame = testBasal.timeFrame
        }
        glucoseRepo.insert(glucose)
        viewModel.runPrepare(glucose.uid, pluginManager)
        viewModel.glucose.observe(lifecycle, Observer {
            assertTrue(viewModel.hasPotentialBasal(it))
        })
    }

    @Test
    fun getInsulinByTimeFrame() = runBlocking {
        val glucose = glucose {
            uid = 91
            timeFrame = testInsulin.timeFrame
        }
        glucoseRepo.insert(glucose)
        viewModel.runPrepare(glucose.uid, pluginManager)
        viewModel.glucose.observe(lifecycle, Observer {
            assertEquals(glucose.timeFrame, viewModel.getInsulinByTimeFrame().timeFrame)
        })
    }

    @Test
    fun applyInsulinSuggestion() = runBlocking {
        val test = 6.5f
        val glucose = glucose {
            uid = 42
        }
        glucoseRepo.insert(glucose)
        viewModel.runPrepare(glucose.uid, pluginManager)

        viewModel.glucose.observe(lifecycle, blockingObserver {
            viewModel.runApplySuggestion(test, testInsulin.uid)

            val fromDb = glucoseRepo.getById(glucose.uid)
            assertEquals(test, fromDb.insulinValue0)
            assertEquals(testInsulin.uid, fromDb.insulinId0)

            // Stop observing or we end up looping forever
            viewModel.glucose.removeObservers(lifecycle)
        })
    }

    private fun <T> blockingObserver(block: suspend (T) -> Unit) = Observer<T> { runBlocking { block(it) } }
}
