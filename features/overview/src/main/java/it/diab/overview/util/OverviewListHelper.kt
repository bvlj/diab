/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import it.diab.core.time.DateTime
import it.diab.core.time.DateTimeFormatter
import it.diab.core.util.PreferencesUtil
import it.diab.overview.R
import it.diab.overview.components.status.GraphData
import it.diab.overview.components.status.HeaderStatus
import it.diab.overview.components.status.LastGlucose

internal class OverviewListHelper(
    private val context: Context
) {

    private var status = HeaderStatus(
        emptyList(),
        LastGlucose.Loading,
        GraphData.Loading
    )

    private val lowIndicator by lazy { buildIndicator(R.color.glucoseIndicator_low) }
    private val highIndicator by lazy { buildIndicator(R.color.glucoseIndicator_high) }
    private val highThreshold by lazy { PreferencesUtil.getGlucoseHighThreshold(context) }
    private val lowThreshold by lazy { PreferencesUtil.getGlucoseLowThreshold(context) }

    private val hourFormat = DateTimeFormatter("HH:mm")

    private lateinit var sharedView: View

    fun setStatus(status: HeaderStatus) {
        this.status = status
    }

    fun setSharedView(view: View) {
        sharedView = view
    }

    fun fetchHourText(date: DateTime): String {
        return hourFormat.format(date)
    }

    fun getIndicator(value: Int) = when {
        value < lowThreshold -> lowIndicator
        value > highThreshold -> highIndicator
        else -> null
    }

    fun getDates() = status.dateList

    fun getLast() = status.last

    fun getGraphData() = status.graphData

    fun getSharedElement(activity: Activity) = ActivityOptionsCompat.makeSceneTransitionAnimation(
        activity,
        sharedView,
        sharedView.transitionName
    )

    private fun buildIndicator(@ColorRes colorId: Int): Drawable? {
        val resources = context.resources
        val color = ContextCompat.getColor(context, colorId)
        val size = resources.getDimensionPixelSize(R.dimen.item_glucose_indicator)
        return DrawableUtils.createRoundDrawable(resources, size, color)
    }
}
