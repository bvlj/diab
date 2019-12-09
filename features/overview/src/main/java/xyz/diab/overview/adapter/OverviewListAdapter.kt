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

package xyz.diab.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import xyz.diab.core.extension.hasSameContent
import xyz.diab.core.extension.isTheSame
import xyz.diab.core.model.Glucose
import xyz.diab.overview.R
import xyz.diab.overview.viewholder.OverviewItemViewHolder

internal class OverviewListAdapter(
    private val callbacks: Callbacks
) : ListAdapter<Glucose, OverviewItemViewHolder>(CALLBACK) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OverviewItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_overview_glucose,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: OverviewItemViewHolder, position: Int) {
        holder.bind(getItem(position), callbacks)
    }

    override fun getItemId(position: Int) =
        getItem(position).id

    private object CALLBACK : DiffUtil.ItemCallback<Glucose>() {
        override fun areItemsTheSame(oldItem: Glucose, newItem: Glucose) =
            oldItem.isTheSame(newItem)

        override fun areContentsTheSame(oldItem: Glucose, newItem: Glucose) =
            oldItem.hasSameContent(newItem)
    }

    interface Callbacks {
        fun onItemClicked(id: Long)
    }
}
