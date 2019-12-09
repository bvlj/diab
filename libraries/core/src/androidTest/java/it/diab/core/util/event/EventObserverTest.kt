/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.util.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EventObserverTest {

    private lateinit var lifecycle: LifecycleOwner

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val lifecycleRegistry = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycle = LifecycleOwner { lifecycleRegistry }
    }

    @Test
    fun testObserver() {
        val event = MutableLiveData(Event(0))
        event.observe(lifecycle, EventObserver { num ->
            Assert.assertTrue(num == 0 || num == 1)
        })

        event.value = Event(1)
        Thread.sleep(500)
    }
}
