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
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.graphics.withTranslation
import androidx.core.text.inSpans
import androidx.recyclerview.widget.RecyclerView
import xyz.diab.ui.R
import xyz.diab.ui.extension.endl
import xyz.diab.ui.extension.layoutIsRtl
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * A [RecyclerView.ItemDecoration] which draws sticky headers for a given list of sessions.
 */
class TimeHeaderDecoration(
    context: Context,
    data: List<Long>,
    private val shiftList: Int = 0
) : RecyclerView.ItemDecoration() {

    private val paint: TextPaint
    private val width: Int
    private val padding: Int

    private val dayFormatter = SimpleDateFormat("dd", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    private val timeTextSizeSpan: AbsoluteSizeSpan
    private val monthTextSizeSpan: AbsoluteSizeSpan
    private val boldSpan = StyleSpan(Typeface.BOLD)

    init {
        val attrs = context.obtainStyledAttributes(
            R.style.AppTheme_TimeHeaders,
            R.styleable.TimeHeader
        )

        paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = attrs.getColor(R.styleable.TimeHeader_android_textColor, Color.BLACK)
        }

        width = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_width, 0)
        padding = attrs.getDimensionPixelSize(R.styleable.TimeHeader_android_padding, 0)

        val timeTextSize = attrs.getDimensionPixelSize(R.styleable.TimeHeader_dayTextSize, 0)
        val monthTextSize = attrs.getDimensionPixelSize(R.styleable.TimeHeader_monthTextSize, 0)
        timeTextSizeSpan = AbsoluteSizeSpan(timeTextSize)
        monthTextSizeSpan = AbsoluteSizeSpan(monthTextSize)

        attrs.recycle()
    }

    private val daySlots: Map<Int, StaticLayout> =
        data.mapIndexed { index, timeStamp ->
            index + shiftList to Calendar.getInstance().apply { timeInMillis = timeStamp }
        }.distinctBy {
            val dateTime = it.second
            (dateTime[Calendar.YEAR] * 1000) + dateTime[Calendar.DAY_OF_YEAR]
        }.map {
            it.first to createHeader(it.second.time)
        }.toMap()

    /**
     * Loop over each child and draw any corresponding headers.
     * We also look back to see if there are any headers _before_ the first header we
     * found i.e. which needs to be sticky.
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (daySlots.isEmpty() || parent.childCount == 0) {
            return
        }

        val isRtl = parent.layoutIsRtl
        if (isRtl) {
            c.apply {
                save()
                translate((parent.width - width).toFloat(), 0f)
            }
        }

        var earliestPosition = Int.MAX_VALUE
        var previousHeaderPosition = -1
        var previousHasHeader = false
        var earliestChild: View? = null
        for (i in parent.childCount - 1 downTo 0) {
            // This should not be null, but observed null at times.
            val child = parent.getChildAt(i) ?: continue

            if (child.y > parent.height || (child.y + child.height) < 0) {
                // Can't see this child
                continue
            }

            val position = parent.getChildAdapterPosition(child)
            if (position < 0) {
                continue
            }
            if (position < earliestPosition) {
                earliestPosition = position
                earliestChild = child
            }

            val header = daySlots[position]
            if (header != null) {
                drawHeader(c, child, header, child.alpha, previousHasHeader)
                previousHeaderPosition = position
                previousHasHeader = true
            } else {
                previousHasHeader = false
            }
        }

        if (earliestChild != null && earliestPosition != previousHeaderPosition) {
            // This child needs a sticky header
            findHeaderBeforePosition(earliestPosition)?.let { stickyHeader ->
                previousHasHeader = previousHeaderPosition - earliestPosition == 1
                drawHeader(c, earliestChild, stickyHeader, 1f, previousHasHeader)
            }
        }

        if (isRtl) {
            c.restore()
        }
    }

    private fun findHeaderBeforePosition(position: Int): StaticLayout? {
        for (headerPos in daySlots.keys.reversed()) {
            if (headerPos < position) {
                return daySlots[headerPos]
            }
        }

        return null
    }

    private fun drawHeader(
        canvas: Canvas,
        child: View,
        header: StaticLayout,
        headerAlpha: Float,
        previousHasHeader: Boolean
    ) {
        val childTop = child.y.toInt()
        val childBottom = childTop + child.height
        var top = (childTop + padding).coerceAtLeast(padding)
        if (previousHasHeader) {
            top = top.coerceAtMost(childBottom - header.height - padding)
        }
        paint.alpha = (headerAlpha * 255).toInt()
        canvas.withTranslation(y = top.toFloat()) {
            header.draw(canvas)
        }
    }

    /**
     * Create a header layout for the given [date].
     */
    private fun createHeader(date: Date): StaticLayout {
        val text = SpannableStringBuilder().apply {
            inSpans(boldSpan) {
                inSpans(timeTextSizeSpan) {
                    append(dayFormatter.format(date))
                }
            }
            endl()
            inSpans(monthTextSizeSpan) {
                append(monthFormatter.format(date))
            }
        }

        return StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(0f, 1f)
            .setIncludePad(false)
            .build()
    }
}
