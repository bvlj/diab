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
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import it.diab.core.override.BaseFitHandler
import it.diab.core.time.DateTime
import it.diab.core.util.Activities
import it.diab.core.util.ComposableError
import it.diab.core.util.PreferencesUtil
import it.diab.core.util.SystemUtil
import it.diab.core.util.extensions.bus
import it.diab.data.plugin.PluginManager
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.glucose.R
import it.diab.glucose.components.EditableComponent
import it.diab.glucose.components.status.EditableInStatus
import it.diab.glucose.components.status.EditableOutStatus
import it.diab.glucose.events.EditorEvents
import it.diab.glucose.suggestion.models.CheckAgainSuggestion
import it.diab.glucose.suggestion.models.InsulinSuggestion
import it.diab.glucose.suggestion.status.CheckAgainStatus
import it.diab.glucose.suggestion.status.InsulinStatus
import it.diab.glucose.suggestion.status.SuggestionStatus
import it.diab.glucose.suggestion.ui.SuggestionUiInterface
import it.diab.glucose.ui.DateTimeDialog
import it.diab.glucose.util.VibrationUtil
import it.diab.glucose.util.extensions.forEachUntilTrueIndexed
import it.diab.glucose.viewmodels.EditorViewModel
import it.diab.glucose.viewmodels.EditorViewModelFactory

internal class EditorFragment : Fragment() {

    private lateinit var viewModel: EditorViewModel
    private val error = ComposableError()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = context ?: return
        val factory = EditorViewModelFactory(
            GlucoseRepository.getInstance(context),
            InsulinRepository.getInstance(context)
        )
        viewModel = ViewModelProvider(this, factory)[EditorViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_editor_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EditableComponent(view, viewModel.viewModelScope, bus)

        val uid = arguments?.getLong(Activities.Glucose.Editor.EXTRA_UID, -1L) ?: -1L

        val context = context ?: return
        viewModel.prepare(uid, PluginManager(context)) {
            // Bind glucose observer now that everything is ready
            viewModel.model.observe(viewLifecycleOwner, Observer(this::setup))
            subscribe()
        }
    }

    private fun setup(model: EditableInStatus) {
        bus.emit(
            EditorEvents.Edit::class,
            EditorEvents.Edit.IntentEdit(model)
        )
    }

    private fun subscribe() {
        bus.subscribe(EditorEvents.Requests::class, viewModel.viewModelScope) {
            when (it) {
                is EditorEvents.Requests.IntentRequestClose -> activity?.onBackPressed()
                is EditorEvents.Requests.IntentRequestDate -> onDateClicked()
                is EditorEvents.Requests.IntentRequestEditInsulin -> onEditInsulin(it.uid, it.isBasal)
                is EditorEvents.Requests.IntentRequestMainAction -> onSaveRequested()
                is EditorEvents.Requests.IntentRequestSuggestion -> onSuggestionsRequested(it.suggestionInterface)
            }
        }

        bus.subscribe(EditorEvents.Listeners::class, viewModel.viewModelScope) {
            when (it) {
                is EditorEvents.Listeners.IntentChangedValue -> onValueChanged(it.value)
            }
        }

        bus.subscribe(EditorEvents.Edit::class, viewModel.viewModelScope) {
            when (it) {
                is EditorEvents.Edit.IntentSave -> onSave(it.status)
            }
        }
    }

    private fun onValueChanged(value: Int) {
        if (value == 0) {
            error += ERROR_VALUE
        } else {
            error -= ERROR_VALUE
        }

        bus.emit(EditorEvents.Listeners::class, EditorEvents.Listeners.IntentChangedValueError(value == 0))
    }

    private fun onEditInsulin(uid: Long, isBasal: Boolean) {
        val fragment = InsulinDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(Activities.Glucose.Editor.EXTRA_UID, uid)
                putBoolean(Activities.Glucose.Editor.EXTRA_INSULIN_BASAL, isBasal)
            }
        }

        childFragmentManager.beginTransaction()
            .add(fragment, "insulin_dialog")
            .commit()
    }

    private fun onDateClicked() {
        val activity = activity ?: return
        DateTimeDialog(activity, this::onDateSelected).show(viewModel.date)
    }

    private fun onDateSelected(timeInMillis: Long) {
        val date = DateTime(timeInMillis)

        if (timeInMillis <= System.currentTimeMillis()) {
            viewModel.date = date
            error -= ERROR_DATE
            bus.emit(
                EditorEvents.Listeners::class,
                EditorEvents.Listeners.IntentChangedDate(date, false)
            )
        } else {
            error += ERROR_DATE
            bus.emit(
                EditorEvents.Listeners::class,
                EditorEvents.Listeners.IntentChangedDate(date, true)
            )
        }
    }

    private fun onSuggestionsRequested(suggestionInterface: SuggestionUiInterface) {
        val context = context ?: return
        val glucose = viewModel.glucose.value ?: return
        val proposedInsulin = viewModel.getInsulinByTimeFrame()

        val setupFunctions = arrayOf(
            this::setupInsulinSuggestion,
            this::setupCheckAgainSuggestion
        )

        val insulinStatus: SuggestionStatus = InsulinStatus(
            glucose.timeFrame,
            glucose.insulinId0 > 0L,
            proposedInsulin.uid,
            proposedInsulin.hasHalfUnits,
            this::onSuggestionApplied
        )
        val checkAgainStatus: SuggestionStatus = CheckAgainStatus(
            glucose.value,
            glucose.timeFrame,
            PreferencesUtil.getGlucoseLowThreshold(context)
        )

        arrayOf(insulinStatus, checkAgainStatus).forEachUntilTrueIndexed { index, status ->
            setupFunctions[index](status, suggestionInterface)
        }
    }

    private fun setupInsulinSuggestion(
        status: SuggestionStatus,
        suggestionInterface: SuggestionUiInterface
    ): Boolean {
        status as InsulinStatus
        val insulinSuggestion = InsulinSuggestion(status)

        if (!suggestionInterface.applyConfig(insulinSuggestion)) {
            return false
        }

        viewModel.getInsulinSuggestion { suggestionInterface.onSuggestionLoaded(it, insulinSuggestion) }
        return true
    }

    private fun setupCheckAgainSuggestion(
        status: SuggestionStatus,
        suggestionInterface: SuggestionUiInterface
    ): Boolean {
        status as CheckAgainStatus
        val context = context ?: return false
        val checkAgainSuggestion = CheckAgainSuggestion(status, context)

        if (!suggestionInterface.applyConfig(checkAgainSuggestion)) {
            return false
        }

        suggestionInterface.onSuggestionLoaded(status.value, checkAgainSuggestion)
        return true
    }

    private fun onSuggestionApplied(value: Float, insulinUid: Long) {
        val view = view ?: return

        viewModel.applyInsulinSuggestion(value, insulinUid) {
            Snackbar.make(
                view, R.string.insulin_suggestion_applied,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun onSaveRequested() {
        if (viewModel.isEditMode && error.hasAny()) {
            bus.emit(
                EditorEvents.Requests::class,
                EditorEvents.Requests.IntentRequestShowError
            )
        }
    }

    private fun onSave(model: EditableOutStatus) {
        val view = view ?: return

        if (error.hasAny()) {
            onError(view)
            return
        }

        viewModel.save(model)

        Handler().postDelayed(this::addToFit, 300)
        Snackbar.make(view, R.string.glucose_editor_saved, 800).show()
    }

    private fun onError(view: View) {
        Snackbar.make(view, R.string.glucose_editor_save_error, Snackbar.LENGTH_LONG).show()
        VibrationUtil.vibrateForError(view.context)
    }

    private fun addToFit() {
        val activity = activity ?: return
        val handler = SystemUtil.getOverrideObject(
            BaseFitHandler::class.java,
            activity,
            R.string.config_class_fit_handler
        )

        val toUpload = viewModel.glucose.value
        if (!handler.hasFit(activity) || toUpload == null) {
            activity.onBackPressed()
            return
        }

        handler.upload(
            activity,
            toUpload,
            !viewModel.isEditMode
        ) { success -> if (success) activity.onBackPressed() }
    }

    companion object {
        private const val ERROR_VALUE = 1
        private const val ERROR_DATE = 1 shl 1
    }
}
