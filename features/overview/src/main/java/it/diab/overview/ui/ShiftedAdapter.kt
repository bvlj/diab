/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.ui

import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

internal abstract class ShiftedAdapter<T, VH : RecyclerView.ViewHolder>(
    config: AsyncDifferConfig<T>,
    private val shift: Int = 0
) : RecyclerView.Adapter<VH>() {

    @Suppress("LeakingThis")
    private val callback = AdapterListUpdateCallback(this)
    private val differ = AsyncPagedListDiffer(ShiftedListUpdateCallback(), config)

    override fun getItemCount() = differ.itemCount + shift

    fun submitList(pagedList: PagedList<T>?) {
        differ.submitList(pagedList)
    }

    /**
     * If [position] is less than [shift], we expect the adapter
     * to properly handle the situation by using the [getItemViewType]
     * in advance and behave accordingly
     */
    fun getItem(position: Int): T? =
        if (position < shift) null // We expect the Adapter to handle this case
        else differ.getItem(position - shift)

    private inner class ShiftedListUpdateCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            callback.onChanged(position + shift, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            callback.onMoved(fromPosition + shift, toPosition + shift)
        }

        override fun onInserted(position: Int, count: Int) {
            callback.onInserted(position + shift, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            callback.onRemoved(position + shift, count)
        }
    }
}
