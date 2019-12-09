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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.diab.core.util.Activities
import it.diab.core.util.extensions.bus
import it.diab.data.entities.Insulin
import it.diab.data.entities.TimeFrame
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.insulin.R
import it.diab.insulin.components.DeleteDialogComponent
import it.diab.insulin.components.EditableComponent
import it.diab.insulin.components.status.EditableInStatus
import it.diab.insulin.events.EditEvent
import it.diab.insulin.viewmodels.EditorViewModel
import it.diab.insulin.viewmodels.EditorViewModelFactory
import it.diab.ui.util.UIUtils
import it.diab.ui.widgets.BottomSheetDialogFragmentExt
import kotlinx.coroutines.launch

internal class EditorFragment : BottomSheetDialogFragmentExt() {

    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val factory = EditorViewModelFactory(
            GlucoseRepository.getInstance(context),
            InsulinRepository.getInstance(context)
        )
        viewModel = ViewModelProvider(this, factory)[EditorViewModel::class.java]
    }

    override fun onCreateDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_insulin_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UIUtils.setWhiteNavBarIfNeeded(view.context, dialog)
        EditableComponent(view, viewModel.viewModelScope, bus)
        DeleteDialogComponent(view.context, viewModel.viewModelScope, bus)

        val uid = arguments?.getLong(Activities.Insulin.EXTRA_EDITOR_UID, -1L) ?: -1L
        viewModel.setInsulin(uid, this::setup)
    }

    private fun setup(insulin: Insulin) {
        bus.subscribe(EditEvent::class, viewModel.viewModelScope) {
            when (it) {
                is EditEvent.IntentSave -> {
                    viewModel.viewModelScope.launch {
                        viewModel.save(it.status)
                        dismiss()
                    }
                }
                is EditEvent.IntentRequestDelete -> {
                    viewModel.viewModelScope.launch {
                        viewModel.delete(it.deleteValues)
                        dismiss()
                    }
                }
            }
        }

        bus.emit(
            EditEvent::class, EditEvent.IntentEdit(
                EditableInStatus(
                    insulin.uid > 0L,
                    insulin.name,
                    insulin.timeFrame.ordinal,
                    TimeFrame.values().map(TimeFrame::nameRes),
                    insulin.hasHalfUnits,
                    insulin.isBasal
                )
            )
        )
    }
}
