/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.components.status

import com.github.mikephil.charting.data.Entry
import it.diab.core.time.DateTime

internal data class HeaderStatus(
    val dateList: List<DateTime>,
    val last: LastGlucose,
    val graphData: GraphData
)

internal sealed class LastGlucose {

    object Empty : LastGlucose()

    object Loading : LastGlucose()

    data class Available(val value: Int) : LastGlucose()
}

internal sealed class GraphData {

    object Empty : GraphData()

    object Loading : GraphData()

    data class Available(
        val today: List<Entry>,
        val average: List<Entry>
    ) : GraphData()
}
