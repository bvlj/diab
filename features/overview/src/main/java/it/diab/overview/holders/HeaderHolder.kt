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
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import it.diab.core.util.Activities
import it.diab.core.util.intentTo
import it.diab.overview.R
import it.diab.overview.components.status.GraphData
import it.diab.overview.components.status.HeaderItemStatus
import it.diab.overview.components.status.LastGlucose
import it.diab.overview.ui.graph.DataSetFactory
import it.diab.overview.ui.graph.OverviewGraphView
import it.diab.ui.util.extensions.inSpans

internal class HeaderHolder(view: View) : BaseHolder(view) {
    private val moreIcon: ImageView =
        itemView.findViewById(R.id.header_more)
    private val lastValueText: TextView =
        itemView.findViewById(R.id.header_last_value)
    private val lastDescriptionText: TextView =
        itemView.findViewById(R.id.header_last_desc)
    private val overviewGraph: OverviewGraphView =
        itemView.findViewById(R.id.header_chart)

    init {
        moreIcon.setOnClickListener {
            it.context.startActivity(intentTo(Activities.Settings))
        }
    }

    fun bind(model: HeaderItemStatus) {
        bindLast(model.last)
        bindGraphData(model.graphData)
    }

    private fun bindLast(model: LastGlucose) {
        when (model) {
            is LastGlucose.Empty -> setLastEmpty()
            is LastGlucose.Available -> setLastAvailable(model.value)
        }
    }

    private fun setLastEmpty() {
        val context = itemView.context
        lastValueText.text = SpannableStringBuilder().apply {
            inSpans(RelativeSizeSpan(0.75f)) {
                append(context.getString(R.string.overview_last_fallback))
            }
        }
        lastDescriptionText.setText(R.string.overview_last_desc_fallback)
    }

    private fun setLastAvailable(value: Int) {
        lastValueText.text = value.toString()
        lastDescriptionText.setText(R.string.overview_last_desc)
    }

    private fun bindGraphData(model: GraphData) {
        when (model) {
            is GraphData.Empty -> setGraphEmpty()
            is GraphData.Loading -> setGraphLoading()
            is GraphData.Available -> setGraphAvailable(model)
        }
    }

    private fun setGraphEmpty() {
        overviewGraph.apply {
            data = null
            setNoDataText(R.string.overview_graph_empty)
        }
    }

    private fun setGraphLoading() {
        overviewGraph.apply {
            data = null
            setNoDataText(R.string.overview_graph_loading)
        }
    }

    private fun setGraphAvailable(model: GraphData.Available) {
        val graphData = buildGraphData(model)
        if (graphData.isEmpty()) {
            return
        }

        overviewGraph.apply {
            data = LineData(graphData)
            invalidate()
        }
    }

    private fun buildGraphData(model: GraphData.Available): List<ILineDataSet> {
        val context = overviewGraph.context
        val list = arrayListOf<ILineDataSet>()
        val factory = DataSetFactory(context)

        factory.getAverage(model.average)?.let { list.add(it) }
        factory.getToday(model.today)?.let { list.add(it) }
        return list
    }
}
