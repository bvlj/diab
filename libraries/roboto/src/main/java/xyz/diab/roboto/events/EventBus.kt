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

package xyz.diab.roboto.events

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

/**
 * It implements a Factory pattern generating [BroadcastChannel]s based on [Event]s.
 * It maintain a map of [BroadcastChannel]s, one per type per instance of EventBusFactory.
 *
 * Based on Netflix' EventBusFactory.
 *
 * @param lifecycleOwner is a LifecycleOwner used to auto dispose based on destroy observable
 */
@OptIn(ExperimentalCoroutinesApi::class)
class EventBus private constructor(private val lifecycleOwner: LifecycleOwner) {

    private val map = HashMap<Class<*>, BroadcastChannel<*>>()

    @Suppress("unused")
    internal val observer = object : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            destroy()
        }
    }

    @Suppress("unchecked_cast")
    fun <T : Event> emit(
        clazz: KClass<T>,
        event: T,
        context: CoroutineContext = Dispatchers.Default
    ): EventBus {
        val channel = map[clazz.java] ?: create(clazz.java)
        CoroutineScope(context).launch {
            channel as BroadcastChannel<T>
            if (channel.isClosedForSend) {
                Log.w(TAG, "Channel already closed, not sending event")
            } else {
                channel.send(event)
            }
        }
        return this
    }

    @Suppress("unchecked_cast")
    fun <T : Event> subscribe(
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

    fun destroy() {
        map.forEach { entry ->
            entry.value.cancel()
        }

        buses.remove(lifecycleOwner)
    }

    private fun <T> create(clazz: Class<T>): BroadcastChannel<T> {
        val subject = BroadcastChannel<T>(1)
        map[clazz] = subject
        return subject
    }

    companion object {
        private const val TAG = "EventBus"

        private val buses = mutableMapOf<LifecycleOwner, EventBus>()

        @JvmStatic
        operator fun get(lifecycleOwner: LifecycleOwner): EventBus {
            var bus = buses[lifecycleOwner]
            if (bus == null) {
                bus = EventBus(lifecycleOwner)
                buses[lifecycleOwner] = bus
                lifecycleOwner.lifecycle.addObserver(bus.observer)
            }

            return bus
        }
    }
}
