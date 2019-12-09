/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.components.views

import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiView
import it.diab.insulin.R
import it.diab.insulin.components.status.EditableInStatus
import it.diab.insulin.components.status.EditableOutStatus
import it.diab.insulin.events.EditEvent

internal class EditableView(
    container: View,
    private val bus: EventBusFactory
) : UiView<EditableInStatus, EditableOutStatus>(container) {

    private val title: TextView =
        container.findViewById(R.id.insulin_edit_dialog_title)

    private val name: AppCompatEditText =
        container.findViewById(R.id.insulin_edit_name)

    private val timeFrame: AppCompatSpinner =
        container.findViewById(R.id.insulin_edit_time)

    private val hasHalfUnits: SwitchCompat =
        container.findViewById(R.id.insulin_edit_half_units)

    private val isBasal: SwitchCompat =
        container.findViewById(R.id.insulin_edit_basal)

    private val save: MaterialButton =
        container.findViewById(R.id.insulin_edit_save)

    private val delete: MaterialButton =
        container.findViewById(R.id.insulin_edit_delete)

    init {
        save.setOnClickListener {
            bus.emit(EditEvent::class, EditEvent.IntentRequestSave)
        }
    }

    override fun setStatus(status: EditableInStatus) {
        title.setText(if (status.isEdit) R.string.insulin_editor_edit else R.string.insulin_editor_add)

        name.setText(status.name)
        timeFrame.adapter = ArrayAdapter(
            timeFrame.context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            status.timeFrameOptions.map(timeFrame.context::getString)
        )
        timeFrame.setSelection(status.timeFrameIndex)

        hasHalfUnits.isChecked = status.hasHalfUnits
        isBasal.isChecked = status.isBasal

        delete.visibility = if (status.isEdit) View.VISIBLE else View.GONE
        delete.setOnClickListener {
            bus.emit(EditEvent::class, EditEvent.IntentAskDelete(status.name))
        }
    }

    override fun getStatus() = EditableOutStatus(
        name.text.toString(),
        timeFrame.selectedItemPosition,
        hasHalfUnits.isChecked,
        isBasal.isChecked
    )
}
