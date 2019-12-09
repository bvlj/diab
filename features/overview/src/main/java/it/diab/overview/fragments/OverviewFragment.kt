/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import it.diab.core.util.Activities
import it.diab.core.util.event.EventObserver
import it.diab.core.util.extensions.bus
import it.diab.core.util.intentTo
import it.diab.data.entities.GlucoseWithInsulin
import it.diab.data.repositories.GlucoseRepository
import it.diab.overview.R
import it.diab.overview.components.OverviewComponent
import it.diab.overview.components.status.HeaderStatus
import it.diab.overview.events.OverviewEvent
import it.diab.overview.util.OverviewListHelper
import it.diab.overview.viewmodels.OverviewViewModel
import it.diab.overview.viewmodels.OverviewViewModelFactory

class OverviewFragment : Fragment() {

    private lateinit var viewModel: OverviewViewModel
    private lateinit var helper: OverviewListHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return

        val factory = OverviewViewModelFactory(GlucoseRepository.getInstance(context))
        viewModel = ViewModelProvider(this, factory)[OverviewViewModel::class.java]
        helper = OverviewListHelper(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OverviewComponent(view, viewModel.viewModelScope, bus, helper)

        bus.subscribe(OverviewEvent::class, viewModel.viewModelScope) {
            if (it is OverviewEvent.ClickEvent) {
                onItemClick(it.uid)
            }
        }

        viewModel.pagedList.observe(viewLifecycleOwner, Observer(this::onListChanged))
        viewModel.headerData.observe(viewLifecycleOwner, EventObserver(this::onHeaderChanged))
    }

    private fun onListChanged(pagedList: PagedList<GlucoseWithInsulin>?) {
        if (pagedList == null) {
            return
        }

        bus.emit(OverviewEvent::class, OverviewEvent.UpdateEvent(pagedList))
        viewModel.requestUpdateHeaderData()
    }

    private fun onHeaderChanged(status: HeaderStatus?) {
        if (status == null) {
            return
        }

        bus.emit(OverviewEvent::class, OverviewEvent.HeaderChangeEvent(status))
    }

    private fun onItemClick(uid: Long) {
        val activity = activity ?: return
        val intent = intentTo(Activities.Glucose.Editor).apply {
            putExtra(Activities.Glucose.Editor.EXTRA_UID, uid)
        }
        val sharedOptions = helper.getSharedElement(activity)
        startActivity(intent, sharedOptions.toBundle())
    }
}
