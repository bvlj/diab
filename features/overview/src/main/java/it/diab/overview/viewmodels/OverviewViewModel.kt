/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.viewmodels

import android.util.SparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import com.github.mikephil.charting.data.Entry
import it.diab.core.time.DateTime
import it.diab.core.time.days
import it.diab.core.util.event.Event
import it.diab.data.entities.Glucose
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.toTimeFrame
import it.diab.data.repositories.GlucoseRepository
import it.diab.overview.components.status.GraphData
import it.diab.overview.components.status.HeaderStatus
import it.diab.overview.components.status.LastGlucose
import it.diab.overview.util.extensions.isZeroOrNan
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class OverviewViewModel internal constructor(
    private val glucoseRepository: GlucoseRepository
) : ViewModel() {

    val pagedList = LivePagedListBuilder(glucoseRepository.pagedList, 5).build()

    private val _headerData = MutableLiveData<Event<HeaderStatus>>()
    val headerData: LiveData<Event<HeaderStatus>> = _headerData

    fun requestUpdateHeaderData() {
        viewModelScope.launch {
            _headerData.value = Event(runUpdateHeaderData())
        }
    }

    private suspend fun runUpdateHeaderData(): HeaderStatus = withContext(Default) {
        val end = DateTime.now
        val start = end - 7L.days()
        val data = glucoseRepository.getInDateRange(start, end)

        val todayDeferred = async { updateToday(data) }
        val avgDeferred = async { updateAverage(data) }

        val last = if (data.isEmpty())
            LastGlucose.Empty
        else
            LastGlucose.Available(data[0].value)

        val dateList = glucoseRepository.getAllDates()

        val graphData = GraphData.Available(
            todayDeferred.await(),
            avgDeferred.await()
        )

        HeaderStatus(dateList, last, graphData)
    }

    private fun updateToday(list: List<Glucose>) =
        list.filter { it.date.isToday() }
            .sortedBy { it.date.epochMillis }
            .map { Entry(it.date.asMinutes().toFloat(), it.value.toFloat()) }
            .distinctBy { it.x }

    private fun updateAverage(list: List<Glucose>): List<Entry> {
        val average = SparseArray<Float>()
        // -1 for "DEPRECATED"
        val numTimeFrames = TimeFrame.values().size - 1

        // Start from 1 to skip element at index 0 ("EXTRA")
        for (i in 1..numTimeFrames) {
            val timeFrame = i.toTimeFrame()
            val lastWeek = list.filter { it.timeFrame == timeFrame }
            val averageValue = lastWeek.sumBy { it.value } / lastWeek.size.toFloat()
            average.put(timeFrame.reprHour, averageValue)
        }

        val result = arrayListOf<Entry>()
        for (i in 0..numTimeFrames) {
            val index = i.toTimeFrame().reprHour
            val value = average.get(index)
            if (value.isZeroOrNan()) {
                continue
            }

            result.add(Entry(index * 60f, value))
        }
        return result.sortedBy { it.x }
    }
}
