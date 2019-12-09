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
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import xyz.diab.ui.R

class DeleteItemDecorator(context: Context) : RecyclerView.ItemDecoration() {
    private val bgDrawable: Drawable

    init {
        bgDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.list_delete_bg))
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val lm = parent.layoutManager
        if (parent.itemAnimator?.isRunning == false || lm == null) {
            super.onDraw(c, parent, state)
            return
        }

        var firstComingUp: View? = null
        var lastComingDown: View? = null
        var top = 0
        var bottom = 0

        for (i in 0..lm.childCount) {
            val child = lm.getChildAt(i) ?: continue
            if (child.translationY < 0) {
                lastComingDown = child
            } else if (child.translationY > 0 && firstComingUp == null) {
                firstComingUp = child
            }
        }

        if (firstComingUp != null && lastComingDown != null) {
            top = lastComingDown.bottom + lastComingDown.translationY.toInt()
            bottom = firstComingUp.top + firstComingUp.translationY.toInt()
        } else if (firstComingUp != null) {
            top = firstComingUp.top
            bottom = firstComingUp.top + firstComingUp.translationY.toInt()
        } else if (lastComingDown != null) {
            top = lastComingDown.bottom + lastComingDown.translationY.toInt()
            bottom = lastComingDown.bottom
        }

        bgDrawable.setBounds(0, top, parent.width, bottom)
        bgDrawable.draw(c)
    }
}
