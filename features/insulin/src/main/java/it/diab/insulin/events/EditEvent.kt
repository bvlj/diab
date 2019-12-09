/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.events

import it.diab.core.arch.ComponentEvent
import it.diab.insulin.components.status.EditableInStatus
import it.diab.insulin.components.status.EditableOutStatus

internal sealed class EditEvent : ComponentEvent {

    class IntentEdit(val status: EditableInStatus) : EditEvent()

    class IntentSave(val status: EditableOutStatus) : EditEvent()

    class IntentAskDelete(val name: String) : EditEvent()

    class IntentRequestDelete(val deleteValues: Boolean) : EditEvent()

    object IntentRequestSave : EditEvent()
}
