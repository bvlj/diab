/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.holders

import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import it.diab.core.arch.EventBusFactory
import it.diab.overview.R
import it.diab.overview.components.status.GlucoseItemStatus
import it.diab.overview.events.OverviewEvent
import it.diab.overview.util.OverviewListHelper
import it.diab.ui.util.extensions.getColorAttr
import it.diab.ui.util.extensions.inSpans
import it.diab.ui.util.extensions.setPreText

internal class GlucoseHolder(
    view: View,
    private val bus: EventBusFactory,
    private val helper: OverviewListHelper
) : BaseHolder(view) {

    private val infoView: AppCompatTextView =
        view.findViewById(R.id.item_glucose_info)
    private val indicatorView: ImageView = view.findViewById(R.id.item_glucose_status)

    fun bind(status: GlucoseItemStatus) {
        itemView.visibility = View.VISIBLE

        infoView.setPreText(buildInfo(status))
        setupValueIndicator(status.value)

        itemView.setOnClickListener {
            bus.emit(OverviewEvent::class, OverviewEvent.ClickEvent(status.uid))
        }
    }

    fun bindLoading() {
        itemView.visibility = View.INVISIBLE
        itemView.setOnClickListener { }
    }

    private fun buildInfo(status: GlucoseItemStatus) = SpannableStringBuilder().apply {
        val glucoseInfo = buildGlucoseInfo(status)
        inSpans(RelativeSizeSpan(1.2f)) {
            append(glucoseInfo)
        }

        val insulinInfo = buildInsulinInfo(status) ?: return@apply
        val insulinColor = itemView.context.getColorAttr(R.style.AppTheme, android.R.attr.textColorSecondary)
        inSpans(ForegroundColorSpan(insulinColor)) {
            append(insulinInfo)
        }
    }

    private fun buildGlucoseInfo(status: GlucoseItemStatus) =
        "${status.value} (${helper.fetchHourText(status.date)})"

    private fun buildInsulinInfo(status: GlucoseItemStatus): String? {
        if (status.insulin == null && status.basal == null) {
            return null
        }

        return StringBuilder().run {
            append('\n')
            status.insulin?.let {
                append(it.getDisplayedString(status.insulinValue))
            }

            status.basal?.let {
                if (status.insulin != null) {
                    append(", ")
                }
                append(it.getDisplayedString(status.basalValue))
            }

            toString()
        }
    }

    private fun setupValueIndicator(value: Int) {
        val indicatorDrawable = helper.getIndicator(value)
        if (indicatorDrawable == null) {
            indicatorView.visibility = View.GONE
        } else {
            indicatorView.visibility = View.VISIBLE
            indicatorView.setImageDrawable(indicatorDrawable)
        }
    }
}
