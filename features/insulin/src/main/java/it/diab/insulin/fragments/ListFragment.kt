/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.diab.core.util.Activities
import it.diab.core.util.extensions.bus
import it.diab.data.repositories.InsulinRepository
import it.diab.insulin.R
import it.diab.insulin.components.ListComponent
import it.diab.insulin.events.ListEvent
import it.diab.insulin.viewmodels.ListViewModel
import it.diab.insulin.viewmodels.ListViewModelFactory

// Android Studio does not recognize usage from FragmentContainerView
@Keep
@Suppress("unused")
internal class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        val context = context ?: return

        val factory = ListViewModelFactory(InsulinRepository.getInstance(context))
        viewModel = ViewModelProvider(this, factory)[ListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_insulin_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ListComponent(view, viewModel.viewModelScope, bus)

        bus.subscribe(ListEvent::class, viewModel.viewModelScope) {
            if (it is ListEvent.ClickEvent) {
                onItemClick(it.uid)
            }
        }

        viewModel.list.observe(viewLifecycleOwner, Observer { list ->
            bus.emit(ListEvent::class, ListEvent.UpdateEvent(list))
        })
    }

    private fun onItemClick(uid: Long) {
        val editorFragment = EditorFragment().apply {
            arguments = Bundle().apply {
                putLong(Activities.Insulin.EXTRA_EDITOR_UID, uid)
            }
        }

        childFragmentManager.beginTransaction()
            .add(editorFragment, "editor")
            .commit()
    }
}
