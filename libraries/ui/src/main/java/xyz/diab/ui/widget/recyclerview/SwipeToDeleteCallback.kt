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

package xyz.diab.ui.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import xyz.diab.ui.R

class SwipeToDeleteCallback(context: Context, listener: Listener) : SwipeStartCallback(listener) {

    private val bgDrawable: Drawable
    private val deleteDrawable: Drawable?
    private val margin: Int

    init {
        bgDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.list_delete_bg))
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete)?.apply {
            setTint(ContextCompat.getColor(context, R.color.list_delete_fg))
        }
        margin = context.resources.getDimensionPixelSize(R.dimen.list_swipe_margin)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (viewHolder.adapterPosition == -1) return

        val view = viewHolder.itemView
        val delDrawable = deleteDrawable ?: return

        bgDrawable.apply {
            setBounds(
                (view.right + dX).toInt(),
                view.top,
                view.right,
                view.bottom
            )
            draw(c)
        }

        val icLeft = view.right - margin - delDrawable.intrinsicWidth
        val icTop = view.top + ((view.bottom - view.top - delDrawable.intrinsicHeight) / 2)
        val icRight = view.right - margin
        val icBottom = icTop + delDrawable.intrinsicHeight

        if (view.right + dX <= icLeft) {
            delDrawable.apply {
                setBounds(icLeft, icTop, icRight, icBottom)
                draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
