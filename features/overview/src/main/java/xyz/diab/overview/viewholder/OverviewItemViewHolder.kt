/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab.overview.viewholder

import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.recyclerview.widget.RecyclerView
import xyz.diab.core.extension.fmt
import xyz.diab.core.extension.isEmpty
import xyz.diab.core.model.Glucose
import xyz.diab.overview.R
import xyz.diab.overview.adapter.OverviewListAdapter
import xyz.diab.ui.extension.endl
import xyz.diab.ui.extension.getColorAttr
import xyz.diab.ui.extension.setPreText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class OverviewItemViewHolder(
    container: View
) : RecyclerView.ViewHolder(container) {

    private val infoView: AppCompatTextView = container.findViewById(R.id.itemOverviewInfo)
    private val timeView: AppCompatTextView = container.findViewById(R.id.itemOverviewTime)

    private val secondaryTextColor =
        itemView.context.getColorAttr(R.style.AppTheme, android.R.attr.textColorSecondary)

    fun bind(item: Glucose, callback: OverviewListAdapter.Callbacks) {
        infoView.setPreText(buildInfo(item))
        timeView.setPreText(buildTime(item))

        itemView.setOnClickListener { callback.onItemClicked(item.id) }
    }

    private fun buildInfo(item: Glucose) = buildSpannedString {
        inSpans(RelativeSizeSpan(1.2f)) {
            append(item.value.fmt())
        }

        val bolusInfo = buildBolusInfo(item)
        if (bolusInfo.isNotEmpty()) {
            endl()
            inSpans(ForegroundColorSpan(secondaryTextColor)) {
                append(bolusInfo)
            }
        }
    }

    private fun buildTime(item: Glucose): CharSequence {
        return SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date(item.timeStamp))
    }

    private fun buildBolusInfo(item: Glucose): CharSequence {
        var bolusStatus = if (item.bolus.isEmpty) NO_BOLUS else HAS_BOLUS
        bolusStatus = bolusStatus or if (item.basalBolus.isEmpty) NO_BOLUS else HAS_BASAL_BOLUS

        val bolusInfo = StringBuilder()
        if (bolusStatus and HAS_BOLUS != NO_BOLUS) {
            bolusInfo.append(BOLUS_INFO.format(item.bolus.name, item.bolus.value))

            if (bolusStatus and HAS_BASAL_BOLUS != NO_BOLUS) {
                bolusInfo.append(BOLUS_SEPARATOR)
            }
        }
        if (bolusStatus and HAS_BASAL_BOLUS != NO_BOLUS) {
            bolusInfo.append(BOLUS_INFO.format(item.basalBolus.name, item.basalBolus.value))
        }

        return bolusInfo.toString()
    }

    private companion object {
        const val BOLUS_SEPARATOR = ", "
        const val BOLUS_INFO = "%1\$s %2\$.1f"
        const val DATE_FORMAT = "HH:mm"

        const val NO_BOLUS = 0
        const val HAS_BOLUS = 1
        const val HAS_BASAL_BOLUS = 1 shl 1
    }
}
