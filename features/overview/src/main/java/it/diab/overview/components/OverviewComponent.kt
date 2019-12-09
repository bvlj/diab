/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.components

import android.view.View
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiComponent
import it.diab.overview.components.status.DataStatus
import it.diab.overview.components.views.OverviewView
import it.diab.overview.events.OverviewEvent
import it.diab.overview.util.OverviewListHelper
import kotlinx.coroutines.CoroutineScope

internal class OverviewComponent(
    container: View,
    scope: CoroutineScope,
    bus: EventBusFactory,
    helper: OverviewListHelper
) : UiComponent {

    private val view = OverviewView(container, bus, helper)

    init {
        bus.subscribe(OverviewEvent::class, scope) {
            when (it) {
                is OverviewEvent.UpdateEvent -> view.setStatus(DataStatus(it.pagedList))
                is OverviewEvent.HeaderChangeEvent -> view.onHeaderChanged(it.status)
            }
        }
    }
}
