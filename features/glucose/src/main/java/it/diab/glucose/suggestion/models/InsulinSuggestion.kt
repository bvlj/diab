/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.suggestion.models

import android.content.res.Resources
import it.diab.data.entities.TimeFrame
import it.diab.data.plugin.PluginManager
import it.diab.glucose.R
import it.diab.glucose.suggestion.status.InsulinStatus
import kotlin.math.roundToInt

internal class InsulinSuggestion(status: InsulinStatus) : SuggestionModel<Float, InsulinStatus>(status) {

    override fun isValid(): Boolean {
        val validTF = when (status.timeFrame) {
            TimeFrame.MORNING,
            TimeFrame.DINNER,
            TimeFrame.LUNCH -> true
            else -> false
        }
        return validTF && !status.hasInsulin
    }

    override val icon = R.drawable.ic_suggestion_ml

    override fun validate(value: Float) = value >= 0

    override fun getFailMessage(value: Float, res: Resources?): String? {
        if (res == null) {
            return null
        }

        return when (value) {
            PluginManager.TOO_HIGH -> res.getString(R.string.insulin_suggestion_warning_high)
            PluginManager.TOO_LOW -> res.getString(R.string.insulin_suggestion_warning_low)
            PluginManager.NO_MODEL -> null
            else -> res.getString(R.string.insulin_suggestion_error)
        }
    }

    override fun getSuccessMessage(value: Float, res: Resources?): String {
        return res?.getString(R.string.insulin_suggestion_value, value.roundToInsulin()) ?: "???"
    }

    override fun onSuggestionApply(value: Float) {
        status.onSuggestionApplied(value.roundToInsulin(), status.proposedInsulinUid)
    }

    private fun Float.roundToInsulin() = if (status.increaseByHalf)
        (this * 2).roundToInt() / 2f
    else
        roundToInt().toFloat()
}
