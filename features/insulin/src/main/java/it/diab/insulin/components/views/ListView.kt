/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.components.views

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiView
import it.diab.core.arch.ViewStatus
import it.diab.insulin.R
import it.diab.insulin.adapters.InsulinAdapter
import it.diab.insulin.components.status.ListStatus
import it.diab.insulin.events.ListEvent
import it.diab.ui.widgets.RecyclerViewExt

internal class ListView(
    container: View,
    bus: EventBusFactory
) : UiView<ListStatus, ViewStatus>(container) {

    private val list: RecyclerViewExt =
        container.findViewById(R.id.insulin_list)

    private val fab: FloatingActionButton =
        container.findViewById(R.id.insulin_fab)

    private val adapter = InsulinAdapter(bus)

    init {
        list.adapter = adapter

        fab.setOnClickListener {
            bus.emit(ListEvent::class, ListEvent.ClickEvent(-1))
        }
    }

    override fun setStatus(status: ListStatus) {
        adapter.submitList(status.pagedList)
    }
}
