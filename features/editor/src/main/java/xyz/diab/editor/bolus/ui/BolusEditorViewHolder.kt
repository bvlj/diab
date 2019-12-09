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

package xyz.diab.editor.bolus.ui

import android.view.View
import android.widget.ArrayAdapter
import kotlinx.coroutines.CoroutineScope
import xyz.diab.core.extension.fmt
import xyz.diab.core.model.Bolus
import xyz.diab.editor.R
import xyz.diab.editor.databinding.UiBolusEditorBinding
import xyz.diab.roboto.events.EventBus
import xyz.diab.ui.extension.valueAsFloat

internal class BolusEditorViewHolder(
    private val binding: UiBolusEditorBinding,
    private val bus: EventBus,
    scope: CoroutineScope
) {

    init {
        bus.subscribe(BolusEditorEvent::class, scope) {
            when (it) {
                is BolusEditorEvent.Setup -> setup(
                    it.glucoseId,
                    it.bolus,
                    it.basal,
                    it.names,
                    it.selection
                )
            }
        }
    }

    private fun setup(
        itemId: Long,
        bolus: Bolus,
        basal: Boolean,
        names: Array<String>,
        selection: Int
    ) {
        val isEmpty = names.isEmpty()
        binding.apply {
            setupTitle(basal)
            setupSettings()
            setupNameSpinner(names, selection, isEmpty)
            setupQuantityField(bolus.value, isEmpty)
            setupSaveBtn(itemId, names, basal, isEmpty)
            setupDeleteBtn(itemId, basal, isEmpty)
        }
    }

    private fun UiBolusEditorBinding.setupTitle(basal: Boolean) {
        title.text = title.context.getString(
            if (basal) R.string.editor_bolus_dialog_title_basal
            else R.string.editor_bolus_dialog_title_insulin
        )
    }

    private fun UiBolusEditorBinding.setupSettings() {
        settingsBtn.setOnClickListener { openInsulinManager() }
    }

    private fun UiBolusEditorBinding.setupNameSpinner(
        names: Array<String>,
        selection: Int,
        isEmpty: Boolean
    ) {
        if (isEmpty) {
            nameSpinner.visibility = View.GONE
            return
        }

        nameSpinner.apply {
            adapter = ArrayAdapter(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                names
            )
            setSelection(selection)
        }
    }

    private fun UiBolusEditorBinding.setupQuantityField(
        currentQuantity: Float,
        isEmpty: Boolean
    ) {
        if (isEmpty) {
            quantityField.visibility = View.GONE
        } else {
            quantityField.setText(currentQuantity.fmt())
        }
    }

    private fun UiBolusEditorBinding.setupSaveBtn(
        itemId: Long,
        names: Array<String>,
        basal: Boolean,
        isEmpty: Boolean
    ) {
        saveBtn.apply {
            if (isEmpty) {
                visibility = View.GONE
            } else {
                text = context.getString(R.string.action_apply)
                setOnClickListener { saveBolus(itemId, names, basal) }
            }
        }
    }

    private fun UiBolusEditorBinding.setupDeleteBtn(
        itemId: Long,
        basal: Boolean,
        isEmpty: Boolean
    ) {
        deleteBtn.apply {
            if (isEmpty) {
                text = context.getString(R.string.editor_bolus_dialog_empty_action)
                setOnClickListener { openInsulinManager() }
            } else {
                setOnClickListener { removeBolus(itemId, basal) }
            }
        }
    }

    private fun openInsulinManager() {
        bus.emit(BolusEditorEvent::class, BolusEditorEvent.NavigateToInsulinManager)
    }

    private fun saveBolus(
        itemId: Long,
        names: Array<String>,
        basal: Boolean
    ) {
        val bolus = Bolus(
            names[binding.nameSpinner.selectedItemPosition],
            binding.quantityField.valueAsFloat(0f)
        )
        bus.emit(BolusEditorEvent::class, BolusEditorEvent.Save(itemId, bolus, basal))
    }

    private fun removeBolus(
        itemId: Long,
        basal: Boolean
    ) {
        bus.emit(BolusEditorEvent::class, BolusEditorEvent.Save(itemId, Bolus(), basal))
    }
}
