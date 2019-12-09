/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.events

import it.diab.core.arch.ComponentEvent
import it.diab.core.time.DateTime
import it.diab.glucose.components.status.EditableInStatus
import it.diab.glucose.components.status.EditableOutStatus
import it.diab.glucose.suggestion.ui.SuggestionUiInterface

internal object EditorEvents {

    sealed class Edit : ComponentEvent {
        class IntentEdit(val status: EditableInStatus) : Edit()

        class IntentSave(val status: EditableOutStatus) : Edit()
    }

    sealed class Listeners : ComponentEvent {
        class IntentChangedValue(val value: Int) : Listeners()

        class IntentChangedDate(val date: DateTime, val hasError: Boolean) : Listeners()

        class IntentChangedValueError(val hasError: Boolean) : Listeners()
    }

    sealed class Requests : ComponentEvent {
        class IntentRequestEditInsulin(val uid: Long, val isBasal: Boolean) : Requests()

        class IntentRequestSuggestion(val suggestionInterface: SuggestionUiInterface) : Requests()

        object IntentRequestDate : Requests()

        object IntentRequestClose : Requests()

        object IntentRequestMainAction : Requests()

        object IntentRequestShowError : Requests()
    }
}
