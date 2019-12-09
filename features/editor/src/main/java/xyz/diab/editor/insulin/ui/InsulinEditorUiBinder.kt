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

package xyz.diab.editor.insulin.ui

import android.view.View
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.CoroutineScope
import xyz.diab.core.model.Insulin
import xyz.diab.editor.R
import xyz.diab.editor.databinding.UiInsulinEditorBinding
import xyz.diab.roboto.events.EventBus

internal class InsulinEditorUiBinder(
    private val binding: UiInsulinEditorBinding,
    private val bus: EventBus,
    scope: CoroutineScope
) {

    private var validName = false

    init {
        bus.subscribe(InsulinEditorEvent::class, scope) {
            when (it) {
                is InsulinEditorEvent.Setup -> setup(it.item)
                is InsulinEditorEvent.OnDuplicatedName -> onDuplicatedName()
            }
        }
    }

    private fun setup(item: Insulin) {
        validName = item.name.isNotBlank()

        binding.apply {
            setupTitle(item)
            setupNameField(item)
            setupBasalField(item)
            setupHalfUnitsField(item)
            setupPositiveBtn(item)
            setupNegativeBtn(item)
        }
    }

    private fun UiInsulinEditorBinding.setupTitle(item: Insulin) {
        title.setText(
            if (item.name.isBlank()) R.string.editor_insulin_title_new
            else R.string.editor_insulin_title_edit
        )
    }

    private fun UiInsulinEditorBinding.setupNameField(item: Insulin) {
        nameField.apply {
            setText(item.name)
            addTextChangedListener { onNameChanged(it.toString()) }
        }
    }

    private fun UiInsulinEditorBinding.setupBasalField(item: Insulin) {
        isBasalField.isChecked = item.basal
    }

    private fun UiInsulinEditorBinding.setupHalfUnitsField(item: Insulin) {
        isHalfUnitsField.isChecked = item.halfUnits
    }

    private fun UiInsulinEditorBinding.setupPositiveBtn(item: Insulin) {
        val isNew = item.name.isEmpty()
        positiveBtn.apply {
            text = context.getString(if (isNew) R.string.action_save else R.string.action_update)
            setOnClickListener { onSave() }
        }
    }

    private fun UiInsulinEditorBinding.setupNegativeBtn(item: Insulin) {
        val isNew = item.name.isEmpty()
        negativeBtn.apply {
            if (isNew) {
                visibility = View.GONE
            } else {
                setOnClickListener { onRemove(item) }
            }
        }
    }

    private fun onDuplicatedName() {
        validName = false
        binding.nameField.apply {
            val currName = text.toString()
            error = context.getString(R.string.editor_error_insulin_duplicated, currName)
        }
    }

    private fun onNameChanged(name: String) {
        val newValid = name.isNotBlank()

        if (newValid != validName) {
            validName = newValid
            binding.positiveBtn.isEnabled = newValid
        }

        binding.nameField.error =
            if (newValid) null
            else binding.nameField.context.getString(R.string.editor_error_name_blank)
    }

    private fun onSave() {
        val item = Insulin(
            binding.nameField.text.toString(),
            binding.isBasalField.isChecked,
            binding.isHalfUnitsField.isChecked
        )
        bus.emit(InsulinEditorEvent::class, InsulinEditorEvent.Save(item))
    }

    private fun onRemove(item: Insulin) {
        bus.emit(InsulinEditorEvent::class, InsulinEditorEvent.Delete(item))
    }
}
