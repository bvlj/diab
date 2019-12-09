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
import it.diab.core.arch.UiComponent
import it.diab.glucose.components.views.EditableView
import it.diab.glucose.events.EditorEvents
import kotlinx.coroutines.CoroutineScope

internal class EditableComponent(
    container: View,
    scope: CoroutineScope,
    bus: EventBusFactory
) : UiComponent {

    private val uiView = EditableView(container, bus)
    private var isEditing = false

    /*
     * Block view updates as for a very brief moment the LiveData db query will
     * change to "default glucose" while inserting the new values in the db
     * so the views would be mistakenly updated with wrong data
     */
    private var blockUpdates = false

    init {
        bus.subscribe(EditorEvents.Requests::class, scope) {
            when (it) {
                is EditorEvents.Requests.IntentRequestMainAction -> {
                    if (isEditing) {
                        blockUpdates = true
                        bus.emit(EditorEvents.Edit::class, EditorEvents.Edit.IntentSave(uiView.getStatus()))
                    } else {
                        isEditing = true
                        uiView.switchToEditMode()
                    }
                }
            }
        }
        bus.subscribe(EditorEvents.Listeners::class, scope) {
            when (it) {
                is EditorEvents.Listeners.IntentChangedValueError -> uiView.setValueError(it.hasError)
                is EditorEvents.Listeners.IntentChangedDate -> uiView.setDate(it.date, it.hasError)
            }
        }
        bus.subscribe(EditorEvents.Edit::class, scope) {
            when (it) {
                is EditorEvents.Edit.IntentEdit -> {
                    if (!blockUpdates) {
                        isEditing = it.status.isEditing
                        uiView.setStatus(it.status)
                    }
                }
            }
        }
    }
}
