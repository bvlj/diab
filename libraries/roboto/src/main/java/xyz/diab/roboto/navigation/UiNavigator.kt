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

package xyz.diab.roboto.navigation

import kotlinx.coroutines.CoroutineScope
import xyz.diab.roboto.events.EventBus

/**
 * UI navigator: allows to move across UIs
 * of different modules using an [EventBus]
 * to signal [NavigationEvent]s
 */
class UiNavigator(private val bus: EventBus) {

    /**
     * Navigate to a new [NavigationTarget]
     */
    fun navigateTo(target: NavigationTarget) {
        bus.emit(NavigationEvent::class, NavigationEvent(target))
    }

    /**
     * Listen to [NavigationEvent]s
     */
    fun addOnNavigationListener(scope: CoroutineScope, listener: NavigationListener) {
        bus.subscribe(NavigationEvent::class, scope) { listener(it.target) }
    }
}