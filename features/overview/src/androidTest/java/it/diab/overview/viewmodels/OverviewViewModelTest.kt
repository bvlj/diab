/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.test.core.app.ApplicationProvider
import it.diab.core.time.DateTime
import it.diab.core.time.hours
import it.diab.core.util.event.EventObserver
import it.diab.data.entities.Glucose
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.glucose
import it.diab.data.extensions.insulin
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.overview.components.status.GraphData
import it.diab.overview.components.status.LastGlucose
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OverviewViewModelTest {
    private lateinit var viewModel: OverviewViewModel
    private lateinit var lifecycle: LifecycleOwner

    private val testTimeFrame = TimeFrame.MORNING
    private val glucoseValues = arrayOf(69, 99, 301, 132)
    private val glucoseArray: Array<Glucose> = Array(4) { i ->
        glucose {
            value = glucoseValues[i]
            timeFrame = testTimeFrame
            date = DateTime.now - (i * 24L).hours()
        }
    }
    private lateinit var glucoseRepository: GlucoseRepository
    private val insulinArray = arrayOf(
        insulin {
            uid = 73
            name = "Foo"
        },
        insulin {
            uid = 42
            name = "Oof"
        }
    )

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Insert test data
        InsulinRepository.getInstance(context).apply {
            setDebugMode()
            insulinArray.forEach { insert(it) }
        }
        glucoseRepository = GlucoseRepository.getInstance(context).apply {
            setDebugMode()
            glucoseArray.forEach { insert(it) }
        }

        viewModel = OverviewViewModel(glucoseRepository)

        val lifecycleRegistry = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycle = LifecycleOwner { lifecycleRegistry }

        Unit
    }

    @Test
    fun getHeaderGraphData() = runBlocking {
        viewModel.headerData.observe(lifecycle, blockingObserver {
            val graphData = it.graphData
            Assert.assertTrue(graphData is GraphData.Available)

            graphData as GraphData.Available
            Assert.assertEquals(
                glucoseValues.average().toFloat(),
                graphData.average[testTimeFrame.ordinal].y
            )
        })

        glucoseRepository.getAllItems()
        viewModel.requestUpdateHeaderData()
    }

    @Test
    fun getHeaderLast() = runBlocking {
        viewModel.headerData.observe(lifecycle, blockingObserver {
            val last = it.last
            Assert.assertTrue(last is LastGlucose.Available)

            last as LastGlucose.Available
            Assert.assertEquals(glucoseValues[0], last.value)
        })

        viewModel.requestUpdateHeaderData()
    }

    private fun <T> blockingObserver(block: suspend (T) -> Unit) = EventObserver<T> { runBlocking { block(it) } }
}
