/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.components.views

import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.android.material.button.MaterialButton
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiView
import it.diab.glucose.R
import it.diab.glucose.components.status.InsulinDialogInStatus
import it.diab.glucose.components.status.InsulinDialogOutStatus
import it.diab.glucose.events.InsulinDialogEvent
import java.util.Locale

internal class InsulinDialogView(
    container: View,
    private val bus: EventBusFactory
) : UiView<InsulinDialogInStatus, InsulinDialogOutStatus>(container) {

    private val constraintView: ConstraintLayout =
        container.findViewById(R.id.glucose_editor_insulin_root)

    private val titleView: TextView =
        container.findViewById(R.id.glucose_editor_insulin_title)

    private val selectorView: AppCompatSpinner =
        container.findViewById(R.id.glucose_editor_insulin_spinner)

    private val quantityView: EditText =
        container.findViewById(R.id.glucose_editor_insulin_value)

    private val editView: ImageView =
        container.findViewById(R.id.glucose_editor_insulin_editor)

    private val positiveButtonView: MaterialButton =
        container.findViewById(R.id.glucose_editor_insulin_positive)

    private val removeButtonView: MaterialButton =
        container.findViewById(R.id.glucose_editor_insulin_negative)

    private val emptyView: TextView =
        container.findViewById(R.id.glucose_editor_insulin_empty)

    init {
        removeButtonView.setOnClickListener {
            bus.emit(
                InsulinDialogEvent::class,
                InsulinDialogEvent.IntentRequestDelete
            )
        }

        editView.setOnClickListener {
            bus.emit(
                InsulinDialogEvent::class,
                InsulinDialogEvent.IntentRequestEditor
            )
        }
    }

    override fun setStatus(status: InsulinDialogInStatus) {
        when (status) {
            is InsulinDialogInStatus.Edit -> setupEdit(status)
            is InsulinDialogInStatus.Empty -> setupEmpty()
        }
    }

    override fun getStatus() = InsulinDialogOutStatus(
        selectorView.selectedItemPosition,
        quantityView.text.toString().toFloatOrNull() ?: 0f
    )

    private fun setupEdit(status: InsulinDialogInStatus.Edit) {
        titleView.setText(
            if (status.isEditing) R.string.glucose_editor_insulin_edit
            else R.string.glucose_editor_insulin_add
        )

        selectorView.adapter = ArrayAdapter(
            selectorView.context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            status.selectableItems
        )
        selectorView.setSelection(status.preferrableIndex)

        if (status.value > 0f) {
            val quantityStr = "%.1f".format(Locale.ROOT, status.value)
            quantityView.setText(quantityStr)
        }

        removeButtonView.visibility = if (status.isEditing) View.VISIBLE else View.GONE

        positiveButtonView.setText(R.string.glucose_editor_insulin_apply)
        positiveButtonView.setOnClickListener {
            bus.emit(
                InsulinDialogEvent::class,
                InsulinDialogEvent.IntentSave(getStatus())
            )
        }
    }

    private fun setupEmpty() {
        applyEmptyLayout()

        emptyView.visibility = View.VISIBLE
        selectorView.visibility = View.GONE
        quantityView.visibility = View.GONE
        removeButtonView.visibility = View.GONE

        positiveButtonView.setOnClickListener {
            bus.emit(
                InsulinDialogEvent::class,
                InsulinDialogEvent.IntentRequestEditor
            )
        }
        positiveButtonView.setText(R.string.glucose_editor_insulin_none_btn)
    }

    private fun applyEmptyLayout() {
        ConstraintSet().apply {
            clone(constraintView)
            connect(
                R.id.glucose_editor_insulin_positive, ConstraintSet.TOP,
                R.id.glucose_editor_insulin_empty, ConstraintSet.BOTTOM
            )
            connect(
                R.id.glucose_editor_insulin_positive, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            connect(
                R.id.glucose_editor_insulin_positive, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
            constrainPercentWidth(
                R.id.glucose_editor_insulin_positive,
                0.8f
            )
            applyTo(constraintView)
        }
    }
}
