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
import it.diab.glucose.components.status.InsulinDialogInStatus
import it.diab.glucose.components.status.InsulinDialogOutStatus

internal sealed class InsulinDialogEvent : ComponentEvent {

    class IntentEdit(val status: InsulinDialogInStatus) : InsulinDialogEvent()

    class IntentSave(val status: InsulinDialogOutStatus) : InsulinDialogEvent()

    object IntentRequestDelete : InsulinDialogEvent()

    object IntentRequestEditor : InsulinDialogEvent()
}
