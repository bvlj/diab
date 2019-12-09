/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.components

import android.view.View
import it.diab.core.arch.EventBusFactory
import it.diab.glucose.components.views.InsulinDialogView
import it.diab.glucose.events.InsulinDialogEvent
import kotlinx.coroutines.CoroutineScope

internal class InsulinDialogComponent(
    container: View,
    scope: CoroutineScope,
    bus: EventBusFactory
) {

    init {
        val uiView = InsulinDialogView(container, bus)

        bus.subscribe(InsulinDialogEvent::class, scope) {
            when (it) {
                is InsulinDialogEvent.IntentEdit -> uiView.setStatus(it.status)
            }
        }
    }
}
