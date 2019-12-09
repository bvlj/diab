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

package xyz.diab.editor.bolus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.diab.core.model.Bolus
import xyz.diab.editor.BottomSheetBaseFragment
import xyz.diab.editor.bolus.ui.BolusEditorEvent
import xyz.diab.editor.bolus.ui.BolusEditorViewHolder
import xyz.diab.editor.bolus.viewmodel.BolusEditorViewModel
import xyz.diab.editor.bolus.viewmodel.BolusEditorViewModelFactory
import xyz.diab.editor.databinding.UiBolusEditorBinding
import xyz.diab.editor.glucose.ui.GlucoseEditorEvent
import xyz.diab.roboto.navigation.NavigationArguments
import xyz.diab.roboto.navigation.NavigationTarget

class BolusEditorFragment : BottomSheetBaseFragment() {

    private lateinit var viewModel: BolusEditorViewModel
    private var _binding: UiBolusEditorBinding? = null
    private val binding: UiBolusEditorBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val bolusEditorViewModel by viewModels<BolusEditorViewModel> {
            BolusEditorViewModelFactory(context.applicationContext)
        }
        viewModel = bolusEditorViewModel
    }

    override fun onCreateDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UiBolusEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BolusEditorViewHolder(binding, bus, viewScope)

        bus.subscribe(BolusEditorEvent::class, viewScope) {
            when (it) {
                is BolusEditorEvent.Save -> onSave(it.glucoseId, it.bolus, it.basal)
                is BolusEditorEvent.NavigateToInsulinManager -> openInsulinManager()
            }
        }

        setup()
    }

    private fun setup() {
        val glucoseId = arguments?.getLong(NavigationArguments.ARG_ID, -1L) ?: -1L
        val basal = arguments?.getBoolean(NavigationArguments.ARG_BASAL, false) ?: false

        viewScope.launch {
            val glucoseItem = viewModel.getGlucose(glucoseId)
            val bolus = if (basal) glucoseItem.basalBolus else glucoseItem.bolus

            val names = viewModel.getInsulinNames(basal)
            var selection = names.indexOf(bolus.name)
            if (selection < 0) selection = 0

            bus.emit(
                BolusEditorEvent::class, BolusEditorEvent.Setup(
                    glucoseId,
                    bolus,
                    basal,
                    names,
                    selection
                )
            )
        }
    }

    private fun onSave(glucoseId: Long, bolus: Bolus, basal: Boolean) {
        viewModel.viewModelScope.launch {
            val newId = viewModel.saveBolus(glucoseId, bolus, basal)

            // If this was a new Glucose (not yet saved),
            // now that it has been added to the db
            // we need to notify the other glucose editor to update
            // its reference so that it can keep in sync
            bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.IdChanged(newId))

            dismiss()
        }
    }

    private fun openInsulinManager() {
        navigator.navigateTo(NavigationTarget.InsulinEditor(null))
        dismiss()
    }
}
