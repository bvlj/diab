/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.components.views

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiView
import it.diab.core.arch.ViewStatus
import it.diab.overview.R
import it.diab.overview.adapters.OverviewAdapter
import it.diab.overview.components.status.DataStatus
import it.diab.overview.components.status.HeaderStatus
import it.diab.overview.events.OverviewEvent
import it.diab.overview.util.OverviewListHelper
import it.diab.ui.util.extensions.doOnNextLayout
import it.diab.ui.util.extensions.removeAllItemDecorators
import it.diab.ui.widgets.RecyclerViewExt
import it.diab.ui.widgets.recyclerview.TimeHeaderDecoration

internal class OverviewView(
    container: View,
    bus: EventBusFactory,
    private val helper: OverviewListHelper
) : UiView<DataStatus, ViewStatus>(container) {

    private val list: RecyclerViewExt =
        container.findViewById(R.id.main_list)
    private val fab: FloatingActionButton =
        container.findViewById(R.id.fab)

    private val adapter = OverviewAdapter(bus, helper)

    init {
        list.adapter = adapter

        helper.setSharedView(fab)
        fab.setOnClickListener {
            bus.emit(OverviewEvent::class, OverviewEvent.ClickEvent(-1))
        }
    }

    override fun setStatus(status: DataStatus) {
        adapter.submitList(status.pagedList)
    }

    fun onHeaderChanged(headerStatus: HeaderStatus) {
        helper.setStatus(headerStatus)
        list.doOnNextLayout {
            list.removeAllItemDecorators()
            list.addItemDecoration(TimeHeaderDecoration(list.context, helper.getDates(), 1))
        }

        // Notify header change
        adapter.notifyItemChanged(0)
    }
}
