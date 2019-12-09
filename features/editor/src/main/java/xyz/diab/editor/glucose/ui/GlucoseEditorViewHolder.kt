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

package xyz.diab.editor.glucose.ui

import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.CoroutineScope
import xyz.diab.core.extension.fmt
import xyz.diab.core.extension.isEmpty
import xyz.diab.core.model.Glucose
import xyz.diab.editor.R
import xyz.diab.editor.databinding.UiGlucoseEditorBinding
import xyz.diab.roboto.events.EventBus
import xyz.diab.ui.extension.onSelectionListener
import xyz.diab.ui.extension.valueAsFloat

internal class GlucoseEditorViewHolder(
    private val binding: UiGlucoseEditorBinding,
    private val bus: EventBus,
    scope: CoroutineScope
) {
    private var item = Glucose()

    private var validValue = true
        set(value) {
            field = value
            onValidityChanged(value)
        }

    init {
        bus.subscribe(GlucoseEditorEvent::class, scope) {
            when (it) {
                is GlucoseEditorEvent.Setup -> setup(it.item)
                is GlucoseEditorEvent.BolusChanged -> onBolusChanged(it.item)
            }
        }

        binding.apply {
            mealField.apply {
                adapter = ArrayAdapter(
                    context,
                    R.layout.support_simple_spinner_dropdown_item,
                    resources.getStringArray(R.array.editor_eat_values)
                )
                setSelection(2)

                onSelectionListener {
                    item = item.copy(eatLevel = it.toEatLevel())
                }
            }

            dateField.apply {
                setOnSelectedListener { date ->
                    item = item.copy(timeStamp = date.time)
                }
            }

            bolusField.setOnActionListener {
                bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.EditBolus(item, false))
            }
            bolusIcon.setOnClickListener {
                bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.EditBolus(item, false))
            }

            basalField.setOnActionListener {
                bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.EditBolus(item, true))
            }
            basalIcon.setOnClickListener {
                bus.emit(GlucoseEditorEvent::class, GlucoseEditorEvent.EditBolus(item, true))
            }

            saveBtn.setOnClickListener { save() }
        }
    }

    private fun setup(item: Glucose) {
        this.item = item

        binding.apply {
            setupToolbar(item)
            setupValueField(item)
            setupEatField(item)
            setupDateField(item)
            setupBolusField(item)
            setupBasalField(item)
            setupSaveButton(item)
        }
    }

    private fun UiGlucoseEditorBinding.setupToolbar(item: Glucose) {
        toolbar.setTitle(
            if (item.value == 0f) R.string.editor_glucose_title_new
            else R.string.editor_glucose_title_edit
        )
    }

    private fun UiGlucoseEditorBinding.setupValueField(item: Glucose) {
        valueField.apply {
            setText(item.value.fmt())

            addTextChangedListener(
                onTextChanged = { editable, _, _, _ ->
                    val value = editable.toString().toFloatOrNull() ?: 0f
                    onValueFieldChanged(value)
                }
            )
        }
    }

    private fun UiGlucoseEditorBinding.setupEatField(item: Glucose) {
        mealField.setSelection(item.eatLevel.toSelection())
    }

    private fun UiGlucoseEditorBinding.setupDateField(item: Glucose) {
        dateField.setDate(item.timeStamp)
    }

    private fun UiGlucoseEditorBinding.setupBolusField(item: Glucose) {
        val validBolus = !item.bolus.isEmpty
        bolusField.text =
            if (validBolus) item.bolus.fmt()
            else bolusField.context.getString(R.string.editor_field_bolus_add)
        bolusIcon.visibility = if (validBolus) View.GONE else View.VISIBLE
    }

    private fun UiGlucoseEditorBinding.setupBasalField(item: Glucose) {
        val validBasal = !item.basalBolus.isEmpty
        basalField.text =
            if (validBasal) item.basalBolus.fmt()
            else basalField.context.getString(R.string.editor_field_basal_add)
        basalIcon.visibility = if (validBasal) View.GONE else View.VISIBLE
    }

    private fun UiGlucoseEditorBinding.setupSaveButton(item: Glucose) {
        val isNew = item.value == 0f
        saveBtn.apply {
            setText(if (isNew) R.string.action_add else R.string.action_save)
        }
    }

    private fun onBolusChanged(newItem: Glucose) {
        // Update the data that may have been changed from the EditorBolus
        item = item.copy(
            id = newItem.id,
            bolus = newItem.bolus,
            basalBolus = newItem.basalBolus
        )

        binding.apply {
            setupBolusField(newItem)
            setupBasalField(newItem)
        }
    }

    private fun onValueFieldChanged(newValue: Float) {
        val isValid = newValue != 0f
        if (isValid == validValue) return

        validValue = isValid
    }

    private fun onValidityChanged(isValid: Boolean) {
        binding.apply {
            valueField.error =
                if (isValid) null
                else valueField.resources.getString(R.string.editor_error_value_invalid)
            saveBtn.isEnabled = isValid
        }
    }

    private fun save() {
        val currentValue = binding.valueField.valueAsFloat(0f)
        if (currentValue == 0f) {
            validValue = false
            return
        }

        bus.emit(
            GlucoseEditorEvent::class, GlucoseEditorEvent.Save(
                item.copy(value = currentValue)
            )
        )
    }

    /*
     *      Index     Level
     * Max      0 <->     2
     * High     1 <->     1
     * Avg      2 <->     0
     * Low      3 <->    -1
     */

    private fun Int.toEatLevel() =
        EAT_LEVELS - 2 - this

    private fun Int.toSelection() =
        (this * -1) + 4 - 2

    companion object {
        private const val EAT_LEVELS = 4
    }
}
