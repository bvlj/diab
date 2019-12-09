/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.components

import android.view.View
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiComponent
import it.diab.insulin.components.status.ListStatus
import it.diab.insulin.components.views.ListView
import it.diab.insulin.events.ListEvent
import kotlinx.coroutines.CoroutineScope

internal class ListComponent(
    container: View,
    scope: CoroutineScope,
    bus: EventBusFactory
) : UiComponent {

    init {
        val view = ListView(container, bus)
        bus.subscribe(ListEvent::class, scope) {
            if (it is ListEvent.UpdateEvent) {
                view.setStatus(ListStatus(it.pagedList))
            }
        }
    }
}
