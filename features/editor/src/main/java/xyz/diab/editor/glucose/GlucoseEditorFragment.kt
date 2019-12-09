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

package xyz.diab.editor.glucose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.diab.core.model.Glucose
import xyz.diab.editor.databinding.UiGlucoseEditorBinding
import xyz.diab.editor.glucose.ui.GlucoseEditorEvent
import xyz.diab.editor.glucose.ui.GlucoseEditorViewHolder
import xyz.diab.editor.glucose.viewmodel.GlucoseEditorViewModel
import xyz.diab.editor.glucose.viewmodel.GlucoseEditorViewModelFactory
import xyz.diab.roboto.BaseFragment
import xyz.diab.roboto.navigation.NavigationArguments
import xyz.diab.roboto.navigation.NavigationTarget

class GlucoseEditorFragment : BaseFragment() {

    private lateinit var viewModel: GlucoseEditorViewModel
    private var _binding: UiGlucoseEditorBinding? = null
    private val binding: UiGlucoseEditorBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val editorViewModel by viewModels<GlucoseEditorViewModel> {
            GlucoseEditorViewModelFactory(
                context.applicationContext
            )
        }
        viewModel = editorViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UiGlucoseEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlucoseEditorViewHolder(binding, bus, viewScope)

        bus.subscribe(GlucoseEditorEvent::class, viewScope) {
            when (it) {
                is GlucoseEditorEvent.EditBolus -> onEditBolus(it.item, it.basal)
                is GlucoseEditorEvent.IdChanged -> onIdChanged(it.newId)
                is GlucoseEditorEvent.Save -> onItemSave(it.item)
            }
        }

        setup()
    }

    private fun setup() {
        val itemId: Long = arguments?.getLong(NavigationArguments.ARG_ID) ?: -1L
        viewScope.launch {
            val item = viewModel.getById(itemId)
            bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.Setup(item))
        }
    }

    private fun onEditBolus(item: Glucose, basal: Boolean) {
        navigator.navigateTo(NavigationTarget.BolusEditor(item.id, basal))
    }

    private fun onIdChanged(newId: Long) {
        viewScope.launch {
            val newItem = viewModel.getById(newId)
            bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.BolusChanged(newItem))
        }
    }

    private fun onItemSave(item: Glucose) {
        viewModel.viewModelScope.launch {
            viewModel.put(item)
            Toast.makeText(context, "SAVED: $item", Toast.LENGTH_LONG).show()
            navigator.navigateTo(NavigationTarget.Back)
        }
    }
}
