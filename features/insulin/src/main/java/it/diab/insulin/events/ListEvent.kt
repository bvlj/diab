/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.events

import androidx.paging.PagedList
import it.diab.core.arch.ComponentEvent
import it.diab.data.entities.Insulin

internal sealed class ListEvent : ComponentEvent {

    class UpdateEvent(val pagedList: PagedList<Insulin>) : ListEvent()

    class ClickEvent(val uid: Long) : ListEvent()
}
