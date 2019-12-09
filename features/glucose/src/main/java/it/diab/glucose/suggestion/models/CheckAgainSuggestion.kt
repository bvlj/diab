/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.suggestion.models

import android.content.Context
import android.content.res.Resources
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import it.diab.data.entities.TimeFrame
import it.diab.glucose.R
import it.diab.glucose.suggestion.status.CheckAgainStatus
import it.diab.glucose.workers.CheckAgainWorker
import java.util.concurrent.TimeUnit

internal class CheckAgainSuggestion(
    status: CheckAgainStatus,
    private val context: Context
) : SuggestionModel<Int, CheckAgainStatus>(status) {

    override fun isValid() = when (status.timeFrame) {
        TimeFrame.MORNING,
        TimeFrame.DINNER,
        TimeFrame.LUNCH -> false
        else -> true
    }

    override val icon = R.drawable.ic_suggestion_remind

    override fun validate(value: Int) = value <= status.lowThreshold

    override fun getFailMessage(value: Int, res: Resources?): String? = null

    override fun getSuccessMessage(value: Int, res: Resources?) =
        res?.getString(R.string.check_again_suggestion_remind) ?: "???"

    override fun onSuggestionApply(value: Int) {
        val workManager = WorkManager.getInstance(context)
        val work = OneTimeWorkRequest.Builder(CheckAgainWorker::class.java)
            .setInitialDelay(15, TimeUnit.MINUTES)
            .build()

        workManager.enqueue(work)
    }
}
