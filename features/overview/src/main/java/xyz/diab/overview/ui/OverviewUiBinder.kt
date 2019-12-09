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

package xyz.diab.overview.ui

import android.content.res.Resources
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import xyz.diab.core.extension.fmt
import xyz.diab.core.model.Glucose
import xyz.diab.overview.R
import xyz.diab.overview.adapter.OverviewListAdapter
import xyz.diab.roboto.events.EventBus
import xyz.diab.ui.extension.endl
import xyz.diab.ui.extension.getColorAttr
import xyz.diab.ui.extension.removeItemDecorationByClass
import xyz.diab.ui.widget.recyclerview.DeleteItemDecorator
import xyz.diab.ui.widget.recyclerview.SwipeStartCallback
import xyz.diab.ui.widget.recyclerview.SwipeToDeleteCallback
import xyz.diab.ui.widget.recyclerview.TimeHeaderDecoration

internal class OverviewUiBinder(
    view: View,
    private val bus: EventBus,
    scope: CoroutineScope
) : OverviewListAdapter.Callbacks {
    private val coordLayout: CoordinatorLayout =
        view.findViewById(R.id.overviewCoordinator)
    private val lastView: TextView =
        view.findViewById(R.id.overviewLast)
    private val recyclerView: RecyclerView =
        view.findViewById(R.id.overviewList)
    private val addFab: FloatingActionButton =
        view.findViewById(R.id.addButton)

    private var snackBar: Snackbar? = null

    private val listAdapter = OverviewListAdapter(this)

    init {
        recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
            addItemDecoration(DeleteItemDecorator(context))
        }

        setupRecyclerViewItemTouchHelper()

        addFab.setOnClickListener {
            onItemClicked(-1L)
        }

        bus.subscribe(OverviewEvent::class, scope) {
            when (it) {
                is OverviewEvent.DataChanged -> onDataChanged(it.dataList)
                is OverviewEvent.ItemDeleted -> onItemDeleted(it.item)
                is OverviewEvent.DeletionUndone -> onDeletionUndone()
            }
        }
    }

    override fun onItemClicked(id: Long) {
        bus.emit(OverviewEvent::class, OverviewEvent.ItemClicked(id))
    }

    private fun onDataChanged(list: List<Glucose>) {
        listAdapter.submitList(list)

        recyclerView.apply {
            removeItemDecorationByClass(TimeHeaderDecoration::class.java)
            addItemDecoration(
                TimeHeaderDecoration(context, list.map(Glucose::timeStamp))
            )
        }

        updateHeader(list.firstOrNull())
    }

    private fun updateHeader(item: Glucose?) {
        val res = lastView.resources
        val secondaryColor = lastView.context.getColorAttr(
            R.style.AppTheme,
            android.R.attr.textColorSecondary
        )
        lastView.text = if (item == null)
            getHeaderEmptySpan(res, secondaryColor)
        else
            getHeaderItemSpan(res, secondaryColor, item)
    }

    private fun getHeaderEmptySpan(res: Resources, @ColorInt secondaryColor: Int) =
        buildSpannedString {
            inSpans(RelativeSizeSpan(HEADER_TITLE_RATIO)) {
                append(res.getString(R.string.overview_last_title_empty))
            }
            endl()
            inSpans(ForegroundColorSpan(secondaryColor)) {
                append(res.getString(R.string.overview_last_summary_empty))
            }
        }

    private fun getHeaderItemSpan(res: Resources, @ColorInt secondaryColor: Int, item: Glucose) =
        buildSpannedString {
            inSpans(RelativeSizeSpan(HEADER_TITLE_RATIO)) {
                inSpans(StyleSpan(Typeface.BOLD)) {
                    append(item.value.fmt())
                }
            }
            endl()
            inSpans(ForegroundColorSpan(secondaryColor)) {
                append(res.getString(R.string.overview_last_summary_item))
            }
        }

    private fun setupRecyclerViewItemTouchHelper() {
        val swipeListener = object : SwipeStartCallback.Listener {
            override fun onSwipeToStart(id: Long) {
                bus.emit(OverviewEvent::class, OverviewEvent.DeleteItem(id))
            }
        }
        val helper = ItemTouchHelper(SwipeToDeleteCallback(recyclerView.context, swipeListener))
        helper.attachToRecyclerView(recyclerView)
    }

    private fun onItemDeleted(item: Glucose) {
        snackBar?.dismiss()

        snackBar = Snackbar.make(coordLayout, R.string.overview_item_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo) {
                bus.emit(OverviewEvent::class, OverviewEvent.DeletionUndone(item))
            }
            .apply(Snackbar::show)
    }

    private fun onDeletionUndone() {
        snackBar?.dismiss()
        snackBar = null
    }

    companion object {
        private const val HEADER_TITLE_RATIO = 3f
    }
}
