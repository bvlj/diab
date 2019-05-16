/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.viewmodels.overview

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import it.diab.core.override.BaseFitHandler
import it.diab.core.util.DateUtils
import it.diab.data.entities.Glucose
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.toTimeFrame
import it.diab.data.repositories.GlucoseRepository
import it.diab.util.extensions.getAsMinutes
import it.diab.util.extensions.isToday
import it.diab.util.extensions.isZeroOrNan
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.set

class OverviewViewModel internal constructor(
    private val glucoseRepository: GlucoseRepository
) : ViewModel() {

    val list = glucoseRepository.all
    val last = glucoseRepository.last

    lateinit var fitHandler: BaseFitHandler

    fun prepare(fHandler: BaseFitHandler) {
        fitHandler = fHandler
    }

    fun getDataSets(block: (List<Entry>, List<Entry>) -> Unit) {
        viewModelScope.launch {
            val end = System.currentTimeMillis()
            val start = end - DateUtils.WEEK
            val data = glucoseRepository.getInDateRange(start, end)
            val result = runGetDataSets(data)

            block(result.first, result.second)
        }
    }

    @SuppressLint("UseSparseArrays")
    @VisibleForTesting
    suspend fun runGetDataSets(data: List<Glucose>) = withContext(Default) {
        val averageDef = async {
            val avg = HashMap<Int, Float>()

            val size = TimeFrame.values().size - 2 // -1 because we start at 0 and -1 for "EXTRA"

            for (i in 0..size) {
                val tf = i.toTimeFrame()

                val lastWeek = data.filter { it.timeFrame == tf }
                val avgVal = lastWeek.indices.map { lastWeek[it].value }
                    .sum() / lastWeek.size.toFloat()
                avg[tf.reprHour] = avgVal
            }

            avg.filterNot { it.value.isZeroOrNan() }
                .map { Entry(it.key * 60f, it.value) }
                .sortedBy { it.x }
        }

        val todayDef = async {
            data.sortedBy { it.date.time }
                .filter { it.date.isToday() }
                .map { Entry(it.date.getAsMinutes(), it.value.toFloat()) }
                .distinctBy { it.x }
        }

        val today = todayDef.await()
        val average = averageDef.await()

        Pair(today, average)
    }
}