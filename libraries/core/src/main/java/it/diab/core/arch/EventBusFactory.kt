/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.arch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

/**
 * It implements a Factory pattern generating [BroadcastChannel]s based on [ComponentEvent]s.
 * It maintain a map of [BroadcastChannel]s, one per type per instance of EventBusFactory.
 *
 * Based on Netflix' EventBusFactory.
 *
 * @param lifecycleOwner is a LifecycleOwner used to auto dispose based on destroy observable
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class EventBusFactory private constructor(private val lifecycleOwner: LifecycleOwner) {

    @ExperimentalCoroutinesApi
    private val map = HashMap<Class<*>, BroadcastChannel<*>>()

    @Suppress("unused")
    internal val observer = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            map.forEach { entry ->
                entry.value.cancel()
            }

            buses.remove(lifecycleOwner)
        }
    }

    fun <T> create(clazz: Class<T>): BroadcastChannel<T> {
        val subject = BroadcastChannel<T>(1)
        map[clazz] = subject
        return subject
    }

    @Suppress("unchecked_cast")
    fun <T : ComponentEvent> emit(
        clazz: KClass<T>,
        event: T,
        context: CoroutineContext = Dispatchers.Default
    ) {
        val channel = map[clazz.java] ?: create(clazz.java)
        CoroutineScope(context).launch {
            channel as BroadcastChannel<T>
            channel.send(event)
        }
    }

    @Suppress("unchecked_cast")
    fun <T : ComponentEvent> subscribe(
        clazz: KClass<T>,
        scope: CoroutineScope,
        onEvent: (T) -> Unit
    ) {
        val channel = map[clazz.java] ?: create(clazz.java)
        channel as BroadcastChannel<T>
        val subscription = channel.openSubscription()
        scope.launch {
            subscription.consumeEach(onEvent)
        }
    }

    companion object {

        private val buses = mutableMapOf<LifecycleOwner, EventBusFactory>()

        @JvmStatic
        operator fun get(lifecycleOwner: LifecycleOwner): EventBusFactory {
            var bus = buses[lifecycleOwner]
            if (bus == null) {
                bus = EventBusFactory(lifecycleOwner)
                buses[lifecycleOwner] = bus
                lifecycleOwner.lifecycle.addObserver(bus.observer)
            }

            return bus
        }
    }
}
