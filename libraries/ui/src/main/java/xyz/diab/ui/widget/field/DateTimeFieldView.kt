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

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import xyz.diab.ui.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimeFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var calendar = Calendar.getInstance()

    private var listener: OnDateTimeSelectedListener? = null

    private val formatter: SimpleDateFormat

    init {
        val typedArray = context.obtainStyledAttributes(
            R.style.AppTheme,
            R.styleable.DateTimeFieldView
        )

        formatter = SimpleDateFormat(
            typedArray.getString(R.styleable.DateTimeFieldView_textFormat) ?: DEFAULT_DATE_FORMAT,
            Locale.getDefault()
        )
        typedArray.recycle()

        maxLines = 1
        background = null

        setOnClickListener {
            showDateDialog()
        }
    }

    fun setDate(timestamp: Long) {
        calendar.timeInMillis = timestamp
        text = formatter.format(calendar.time)
    }

    fun setOnSelectedListener(listener: OnDateTimeSelectedListener) {
        this.listener = listener
    }

    private fun showDateDialog() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            showTimeDialog(year, month, dayOfMonth)
        }

        DatePickerDialog(
            context,
            R.style.AppTheme_DatePickerDialog,
            listener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun showTimeDialog(year: Int, month: Int, dayOfMonth: Int) {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(year, month, dayOfMonth, hourOfDay, minute)
            setText(formatter.format(calendar.time))
            listener?.invoke(calendar.time)
        }
        TimePickerDialog(
            context,
            R.style.AppTheme_DatePickerDialog,
            listener,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true
        ).show()
    }

    companion object {
        private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm"
    }
}
