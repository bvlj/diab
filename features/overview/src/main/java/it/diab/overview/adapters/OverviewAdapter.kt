/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import it.diab.core.arch.EventBusFactory
import it.diab.data.entities.GlucoseWithInsulin
import it.diab.overview.R
import it.diab.overview.components.status.GlucoseItemStatus
import it.diab.overview.components.status.HeaderItemStatus
import it.diab.overview.holders.BaseHolder
import it.diab.overview.holders.GlucoseHolder
import it.diab.overview.holders.HeaderHolder
import it.diab.overview.ui.ShiftedAdapter
import it.diab.overview.util.OverviewListHelper

internal class OverviewAdapter(
    private val bus: EventBusFactory,
    private val helper: OverviewListHelper
) : ShiftedAdapter<GlucoseWithInsulin, BaseHolder>(CONFIG, 1) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == VIEW_HEADER) {
            HeaderHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            )
        } else {
            GlucoseHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_glucose, parent, false),
                bus,
                helper
            )
        }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when (holder) {
            is HeaderHolder -> bindHeader(holder)
            is GlucoseHolder -> bindGlucose(holder, position)
        }
    }

    override fun getItemViewType(position: Int) =
        if (position == 0) VIEW_HEADER else VIEW_GLUCOSE

    private fun bindHeader(holder: HeaderHolder) {
        holder.bind(
            HeaderItemStatus(
                helper.getLast(),
                helper.getGraphData()
            )
        )
    }

    private fun bindGlucose(holder: GlucoseHolder, position: Int) {
        val item = getItem(position)
        if (item == null) {
            holder.bindLoading()
            return
        }

        val glucose = item.glucose

        holder.bind(
            GlucoseItemStatus(
                glucose.uid,
                glucose.value,
                glucose.date,
                item.insulin,
                glucose.insulinValue0,
                item.basal,
                glucose.insulinValue1
            )
        )
    }

    companion object {
        private val CONFIG =
            AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<GlucoseWithInsulin>() {
                override fun areContentsTheSame(oldItem: GlucoseWithInsulin, newItem: GlucoseWithInsulin) =
                    oldItem.glucose == newItem.glucose

                override fun areItemsTheSame(oldItem: GlucoseWithInsulin, newItem: GlucoseWithInsulin) =
                    oldItem.glucose.uid == newItem.glucose.uid
        }).build()

        private const val VIEW_HEADER = 0
        private const val VIEW_GLUCOSE = 1
    }
}
