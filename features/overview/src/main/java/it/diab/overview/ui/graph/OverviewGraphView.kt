/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.ui.graph

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.utils.Utils
import it.diab.overview.R

internal class OverviewGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr) {

    init {
        // Legend
        mLegend.isEnabled = false
        description.isEnabled = false

        // Zoom
        setPinchZoom(false)
        isDoubleTapToZoomEnabled = false
        setScaleEnabled(false)

        // Axis
        xAxis.apply {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            isEnabled = false
        }

        axisRight.apply {
            setDrawAxisLine(false)
            setDrawLabels(false)
            isEnabled = false
        }

        axisLeft.apply {
            setDrawAxisLine(false)
            setDrawLabels(false)
            isEnabled = false
        }

        // Marker
        marker = OverviewMarker(context)

        // Empty status
        setNoDataTextColor(R.color.colorAccentDark)

        // Offset
        val horizontalOffset = context.resources.getDimension(R.dimen.overview_graph_offset)
        setExtraOffsets(horizontalOffset, 0f, horizontalOffset, 0f)

        // No Data text
        setNoDataTextColor(ContextCompat.getColor(context, R.color.textSecondary))
        setNoDataText(R.string.overview_graph_empty)
        mInfoPaint.textSize = Utils.convertDpToPixel(16f)
    }

    fun setNoDataText(@StringRes stringId: Int) {
        setNoDataText(context.getString(stringId))
    }
}
