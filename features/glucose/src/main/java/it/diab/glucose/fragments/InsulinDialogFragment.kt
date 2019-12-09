/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.diab.core.util.Activities
import it.diab.core.util.extensions.bus
import it.diab.core.util.intentTo
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.glucose.R
import it.diab.glucose.components.InsulinDialogComponent
import it.diab.glucose.components.status.InsulinDialogInStatus
import it.diab.glucose.components.status.InsulinDialogOutStatus
import it.diab.glucose.events.InsulinDialogEvent
import it.diab.glucose.viewmodels.InsulinDialogViewModel
import it.diab.glucose.viewmodels.InsulinDialogViewModelFactory
import it.diab.ui.util.UIUtils
import it.diab.ui.widgets.BottomSheetDialogFragmentExt
import kotlinx.coroutines.launch

internal class InsulinDialogFragment : BottomSheetDialogFragmentExt() {
    private lateinit var viewModel: InsulinDialogViewModel

    private var wantsBasal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val factory = InsulinDialogViewModelFactory(
            GlucoseRepository.getInstance(context),
            InsulinRepository.getInstance(context)
        )

        viewModel = ViewModelProvider(this, factory)[InsulinDialogViewModel::class.java]
    }

    override fun onCreateDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_add_insulin, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = arguments?.getLong(Activities.Glucose.Editor.EXTRA_UID, -1L) ?: -1L
        wantsBasal = arguments?.getBoolean(Activities.Glucose.Editor.EXTRA_INSULIN_BASAL) ?: false

        viewModel.prepare(uid, wantsBasal, this::setup)
    }

    private fun setup(status: InsulinDialogInStatus) {
        val view = view ?: return

        UIUtils.setWhiteNavBarIfNeeded(view.context, dialog)
        InsulinDialogComponent(view, viewModel.viewModelScope, bus)

        bus.subscribe(InsulinDialogEvent::class, viewModel.viewModelScope) {
            when (it) {
                is InsulinDialogEvent.IntentRequestDelete -> onDelete()
                is InsulinDialogEvent.IntentSave -> onSave(it.status)
                is InsulinDialogEvent.IntentRequestEditor -> openEditor()
            }
        }

        bus.emit(InsulinDialogEvent::class, InsulinDialogEvent.IntentEdit(status))
    }

    private fun onDelete() {
        viewModel.viewModelScope.launch {
            viewModel.removeInsulin()
            dismiss()
        }
    }

    private fun onSave(status: InsulinDialogOutStatus) {
        viewModel.viewModelScope.launch {
            viewModel.setInsulin(status)
            dismiss()
        }
    }

    private fun openEditor() {
        startActivity(intentTo(Activities.Insulin))
    }
}
