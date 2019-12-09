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

package xyz.diab.ui.widget.field

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.R.attr

class SingleChoiceFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = attr.editTextStyle
) : ActionableFieldView(context, attrs, defStyleAttr) {
    private var selected = 0
    private var options = emptyArray<String>()
    private var listener: OnOptionSelectedListener? = null

    init {
        isFocusableInTouchMode = false

        setOnClickListener { onClick() }
    }

    fun setOptions(@ArrayRes array: Int, default: Int = 0) {
        options = resources.getStringArray(array)
        setSelected(default)
    }

    fun setSelected(index: Int) {
        selected = index
        setText(options[index])
    }

    fun setOnSelectionListener(listener: OnOptionSelectedListener) {
        this.listener = listener
    }

    override fun onClick() {
        if (options.isEmpty()) {
            error("setOptions(Int[, Int]) must be called before handling click events")
        }

        AlertDialog.Builder(context)
            .setTitle(hint)
            .setSingleChoiceItems(options, selected) { d, index ->
                d.dismiss()
                setText(options[index])
                listener?.invoke(index)
            }
            .show()
    }
}
