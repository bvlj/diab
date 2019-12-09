/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.holders

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import it.diab.core.arch.EventBusFactory
import it.diab.insulin.R
import it.diab.insulin.components.status.ListItemStatus
import it.diab.insulin.events.ListEvent
import it.diab.ui.util.extensions.getColorAttr
import it.diab.ui.util.extensions.inSpans
import it.diab.ui.util.extensions.setPreText
import java.util.Locale

internal class InsulinHolder(
    view: View,
    private val bus: EventBusFactory
) : RecyclerView.ViewHolder(view) {
    private val titleView = view.findViewById<AppCompatTextView>(R.id.item_insulin_name)

    @SuppressLint("ResourceType")
    private val secondaryColor = view.context.getColorAttr(
        R.style.AppTheme,
        android.R.attr.textColorSecondary
    )

    fun onBind(status: ListItemStatus) {
        val context = itemView.context
        titleView.setPreText(buildInfo(context, status))

        itemView.setOnClickListener {
            bus.emit(ListEvent::class, ListEvent.ClickEvent(status.uid))
        }
    }

    fun onLoading() {
        itemView.visibility = View.GONE
    }

    private fun buildInfo(context: Context, status: ListItemStatus) =
        SpannableStringBuilder().apply {
            inSpans(RelativeSizeSpan(1.2f)) { append(status.name) }

            append('\n')

            inSpans(ForegroundColorSpan(secondaryColor)) {
                append(context.getString(status.timeFrameRes))

                if (status.isBasal) {
                    append(" (")
                    append(
                        context.getString(R.string.insulin_editor_basal).toLowerCase(Locale.ROOT)
                    )
                    append(")")
                }
            }
        }
}
