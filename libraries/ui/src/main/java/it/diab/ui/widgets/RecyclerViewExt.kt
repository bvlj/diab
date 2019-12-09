/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.diab.ui.R

class RecyclerViewExt @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : RecyclerView(context, attrs, style) {

    init {
        val gridLayout: Boolean
        val gridColumns: Int

        if (attrs == null) {
            gridLayout = false
            gridColumns = 1
        } else {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewExt, 0, 0)
            gridLayout = array.getBoolean(R.styleable.RecyclerViewExt_gridLayout, false)
            gridColumns = array.getInt(R.styleable.RecyclerViewExt_gridColumns, 1)
            array.recycle()
        }

        itemAnimator = DefaultItemAnimator()
        layoutManager = if (gridLayout)
            GridLayoutManager(context, gridColumns)
        else
            LinearLayoutManager(context)
    }
}
