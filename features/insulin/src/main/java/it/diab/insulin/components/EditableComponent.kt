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
import it.diab.insulin.components.views.EditableView
import it.diab.insulin.events.EditEvent
import kotlinx.coroutines.CoroutineScope

internal class EditableComponent(
    container: View,
    scope: CoroutineScope,
    bus: EventBusFactory
) : UiComponent {

    private val uiView = EditableView(container, bus)

    init {
        bus.subscribe(EditEvent::class, scope) {
            when (it) {
                is EditEvent.IntentEdit -> uiView.setStatus(it.status)
                is EditEvent.IntentRequestSave -> {
                    bus.emit(EditEvent::class, EditEvent.IntentSave(uiView.getStatus()))
                }
            }
        }
    }
}
