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
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiView
import it.diab.core.time.DateTime
import it.diab.glucose.R
import it.diab.glucose.components.status.EditableInStatus
import it.diab.glucose.components.status.EditableOutStatus
import it.diab.glucose.events.EditorEvents
import it.diab.glucose.suggestion.ui.SuggestionView
import it.diab.glucose.util.extensions.getDetailedString
import it.diab.glucose.util.extensions.setIconErrorStatus
import it.diab.glucose.util.extensions.setTextErrorStatus
import it.diab.glucose.widget.EatBar
import it.diab.glucose.widget.NumericKeyboardView

internal class EditableView(
    container: View,
    private val bus: EventBusFactory
) : UiView<EditableInStatus, EditableOutStatus>(container) {

    private val constraint: ConstraintLayout =
        container.findViewById(R.id.editor_root)

    private val valueView: TextView =
        container.findViewById(R.id.editor_value)

    private val dateView: TextView =
        container.findViewById(R.id.editor_time)

    private val eatView: EatBar =
        container.findViewById(R.id.editor_eat_level)

    private val insulinView: TextView =
        container.findViewById(R.id.editor_insulin)

    private val basalView: TextView =
        container.findViewById(R.id.editor_basal)

    private val suggestionView: SuggestionView =
        container.findViewById(R.id.editor_suggestion)

    private val keyboardView: NumericKeyboardView =
        container.findViewById(R.id.editor_keyboard)

    private val fabView: FloatingActionButton =
        container.findViewById(R.id.editor_fab)

    private val closeView: ImageView =
        container.findViewById(R.id.editor_close)

    private var hasValueError = false
    private var hasDateError = false

    init {
        constraint.loadLayoutDescription(R.xml.constraint_editor_status)

        closeView.setOnClickListener {
            bus.emit(EditorEvents.Requests::class, EditorEvents.Requests.IntentRequestClose)
        }

        fabView.setOnClickListener {
            bus.emit(EditorEvents.Requests::class, EditorEvents.Requests.IntentRequestMainAction)
        }
    }

    override fun setStatus(status: EditableInStatus) {
        valueView.text = status.value.toString()
        dateView.text = status.date.getDetailedString()
        eatView.setProgress(status.foodIntake)

        if (status.isEditing) {
            setupEdit(false)
        } else {
            setupView(status)
        }
    }

    override fun getStatus() = EditableOutStatus(
        keyboardView.input,
        eatView.getProgress()
    )

    fun setValueError(hasError: Boolean) {
        if (hasError == hasValueError) {
            return
        }

        hasValueError = hasError
        valueView.setTextErrorStatus(hasError)
    }

    fun setDate(date: DateTime, hasError: Boolean) {
        dateView.text = date.getDetailedString()

        if (hasError == hasDateError) {
            return
        }

        hasDateError = hasError
        dateView.setIconErrorStatus(hasError)
    }

    fun switchToEditMode() {
        setupEdit(true)
    }

    private fun setupView(status: EditableInStatus) {
        constraint.setState(R.id.editor_state_view, 0, 0)

        dateView.isEnabled = false
        eatView.isEnabled = false
        fabView.setImageResource(R.drawable.ic_edit)
        setupInsulins(status)

        bus.emit(
            EditorEvents.Requests::class,
            EditorEvents.Requests.IntentRequestSuggestion(suggestionView)
        )
    }

    private fun setupInsulins(status: EditableInStatus) {
        insulinView.setOnClickListener {
            bus.emit(
                EditorEvents.Requests::class,
                EditorEvents.Requests.IntentRequestEditInsulin(status.glucoseUid, false)
            )
        }

        insulinView.text = if (status.insulinUid > 0L) status.insulinDescription
        else insulinView.context.getString(R.string.glucose_editor_insulin_add)

        if (!status.supportsBasal) {
            basalView.visibility = View.GONE
            return
        }

        basalView.setOnClickListener {
            bus.emit(
                EditorEvents.Requests::class,
                EditorEvents.Requests.IntentRequestEditInsulin(status.glucoseUid, true)
            )
        }

        basalView.text = if (status.basalUid > 0L) status.basalDescription
        else insulinView.context.getString(R.string.glucose_editor_basal_add)

        basalView.visibility = View.VISIBLE
    }

    private fun setupEdit(animate: Boolean) {
        keyboardView.bindTextView(valueView, this::onValueChanged)
        dateView.setOnClickListener {
            bus.emit(EditorEvents.Requests::class, EditorEvents.Requests.IntentRequestDate)
        }
        fabView.setImageResource(R.drawable.ic_done)

        constraint.setState(R.id.editor_state_edit, 0, 0)
        dateView.isEnabled = true
        eatView.isEnabled = true
        if (animate) {
            TransitionManager.beginDelayedTransition(constraint)
        }
    }

    private fun onValueChanged(value: Int) {
        bus.emit(EditorEvents.Listeners::class, EditorEvents.Listeners.IntentChangedValue(value))
    }
}
