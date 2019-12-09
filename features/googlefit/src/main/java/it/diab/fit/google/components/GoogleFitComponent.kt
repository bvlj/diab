/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.fit.google.components

import android.view.View
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiComponent
import it.diab.fit.google.components.status.GoogleFitStatus
import it.diab.fit.google.components.views.GoogleFitView
import it.diab.fit.google.events.GoogleFitEvents
import kotlinx.coroutines.CoroutineScope

internal class GoogleFitComponent(
    container: View,
    scope: CoroutineScope,
    bus: EventBusFactory
) : UiComponent {

    private val view = GoogleFitView(container, bus)

    init {
        bus.subscribe(GoogleFitEvents::class, scope) {
            when (it) {
                is GoogleFitEvents.SetupEvent -> view.setStatus(
                    if (it.isConnected) GoogleFitStatus.Connected(true)
                    else GoogleFitStatus.Disconnected(true)
                )
                is GoogleFitEvents.OnConnectedEvent -> view.setStatus(GoogleFitStatus.Connected(it.success))
                is GoogleFitEvents.OnDisconnectedEvent -> view.setStatus(GoogleFitStatus.Disconnected(it.success))
                is GoogleFitEvents.OnDataDeletedEvent -> view.setStatus(GoogleFitStatus.DataDeleted(it.success))
            }
        }
    }
}
