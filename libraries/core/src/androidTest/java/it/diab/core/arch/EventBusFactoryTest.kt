/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.arch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import it.diab.core.util.extensions.bus
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EventBusFactoryTest {

    private lateinit var lifecycleRegistry: LifecycleRegistry
    private lateinit var lifecycle: LifecycleOwner

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() = runBlocking {
        lifecycleRegistry = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        lifecycle = LifecycleOwner { lifecycleRegistry }
        Unit
    }

    @Test
    fun testSubscribeEmit() = runBlocking {
        val bus = lifecycle.bus
        bus.subscribe(TestEvent::class, this) {
            Assert.assertEquals(1, it.num)

            // Kill the subscription by sending onDestroy()
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }

        bus.emit(TestEvent::class, TestEvent(1))
        delay(500)
    }

    inner class TestEvent(val num: Int) : ComponentEvent
}
