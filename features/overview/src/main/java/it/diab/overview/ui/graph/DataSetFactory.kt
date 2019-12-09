/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.ui.graph

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import it.diab.overview.R

internal class DataSetFactory(private val context: Context) {

    fun getToday(entries: List<Entry>): LineDataSet? {
        if (entries.isEmpty()) {
            return null
        }

        val res = context.resources

        val setColor = ContextCompat.getColor(context, R.color.overviewGraph_today)
        val textColor = ContextCompat.getColor(context, R.color.textPrimary)
        val textSizeDp = res.getDimension(R.dimen.overview_graph_text)
        val lineWidthDp = res.getDimension(R.dimen.overview_graph_line_thickness)

        return LineDataSet(entries, "").apply {
            setCircleColor(setColor)
            color = setColor
            highLightColor = setColor
            lineWidth = lineWidthDp
            circleRadius = lineWidthDp * 1.5f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = textSizeDp
            valueTextColor = textColor
            valueFormatter = OverviewValueFormatter()

            setDrawCircleHole(false)
        }
    }

    fun getAverage(entries: List<Entry>): LineDataSet? {
        if (entries.isEmpty()) {
            return null
        }

        val setColor = ContextCompat.getColor(context, R.color.overviewGraph_average)

        return LineDataSet(entries, "").apply {
            color = setColor
            isHighlightEnabled = false
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 0f

            setDrawCircles(false)
        }
    }
}
