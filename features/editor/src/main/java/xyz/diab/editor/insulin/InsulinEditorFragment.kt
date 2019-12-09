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

package xyz.diab.editor.insulin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.diab.core.model.Insulin
import xyz.diab.editor.BottomSheetBaseFragment
import xyz.diab.editor.databinding.UiInsulinEditorBinding
import xyz.diab.editor.insulin.ui.InsulinEditorEvent
import xyz.diab.editor.insulin.ui.InsulinEditorUiBinder
import xyz.diab.editor.insulin.viewmodel.InsulinEditorViewModel
import xyz.diab.editor.insulin.viewmodel.InsulinEditorViewModelFactory
import xyz.diab.roboto.navigation.NavigationArguments

class InsulinEditorFragment : BottomSheetBaseFragment() {

    private lateinit var viewModel: InsulinEditorViewModel
    private var _binding: UiInsulinEditorBinding? = null
    private val binding: UiInsulinEditorBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val editorViewModel by viewModels<InsulinEditorViewModel> {
            InsulinEditorViewModelFactory(context)
        }
        viewModel = editorViewModel
    }

    override fun onCreateDialogView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UiInsulinEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InsulinEditorUiBinder(binding, bus, viewScope)

        bus.subscribe(InsulinEditorEvent::class, viewScope) {
            when (it) {
                is InsulinEditorEvent.Delete -> onDelete(it.item)
                is InsulinEditorEvent.Save -> onSave(it.item)
            }
        }

        setup()
    }

    private fun setup() {
        val name = arguments?.getString(NavigationArguments.ARG_NAME)
        viewScope.launch {
            val item = if (name == null) Insulin() else viewModel.getByName(name)
            bus.emit(InsulinEditorEvent::class, InsulinEditorEvent.Setup(item))
        }
    }

    private fun onDelete(item: Insulin) {
        viewModel.viewModelScope.launch {
            viewModel.delete(item)
        }
    }

    private fun onSave(item: Insulin) {
        viewModel.viewModelScope.launch {
            val validName = item.name.isBlank() || viewModel.nameExists(item.name)
            if (!validName) {
                viewModel.put(item)
                dismiss()
            } else {
                bus.emit(InsulinEditorEvent::class, InsulinEditorEvent.OnDuplicatedName)
            }
        }
    }
}
