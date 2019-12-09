/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.diab.core.model.Glucose
import xyz.diab.overview.ui.OverviewEvent
import xyz.diab.overview.ui.OverviewUiBinder
import xyz.diab.overview.viewmodel.OverviewViewModel
import xyz.diab.overview.viewmodel.OverviewViewModelFactory
import xyz.diab.roboto.BaseFragment
import xyz.diab.roboto.navigation.NavigationTarget

class OverviewFragment : BaseFragment() {

    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val overviewViewModel by viewModels<OverviewViewModel> {
            OverviewViewModelFactory(context.applicationContext)
        }
        viewModel = overviewViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.ui_overview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OverviewUiBinder(view, bus, viewScope)
        viewModel.liveList.observe(viewLifecycleOwner, Observer {
            bus.emit(OverviewEvent::class, OverviewEvent.DataChanged(it))
        })

        bus.subscribe(OverviewEvent::class, viewScope) {
            when (it) {
                is OverviewEvent.ItemClicked -> onItemClicked(it.id)
                is OverviewEvent.DeleteItem -> onItemDeletion(it.id)
                is OverviewEvent.DeletionUndone -> onDeletionUndone(it.item)
            }
        }
    }

    private fun onItemClicked(id: Long) {
        navigator.navigateTo(NavigationTarget.GlucoseEditor(id))
    }

    private fun onItemDeletion(id: Long) {
        viewModel.viewModelScope.launch {
            val deleted = viewModel.deleteItemAtIndex(id)
            bus.emit(OverviewEvent::class, OverviewEvent.ItemDeleted(deleted))
        }
    }

    private fun onDeletionUndone(item: Glucose) {
        viewModel.viewModelScope.launch {
            viewModel.put(item)
        }
    }
}
