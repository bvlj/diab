/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import it.diab.core.time.DateTime
import it.diab.core.time.days
import it.diab.glucose.R

internal class DateTimeDialog(
    private val activity: Activity,
    private val onSelected: (Long) -> Unit
) : DialogInterface.OnClickListener {

    private var dateTime = DateTime.now

    private val timePickerCallback = { _: View, hour: Int, minute: Int ->
        dateTime = dateTime.with(DateTime.HOUR, hour)
            .with(DateTime.MINUTE, minute)

        onSelected(dateTime.epochMillis)
    }

    private val datePickerCallback = { _: View, year: Int, month: Int, day: Int ->
        dateTime = DateTime(year, month, day)

        showTimePicker()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            0 -> {
                dialog?.dismiss()
                dateTime = dateTime.with(DateTime.DAY_OF_YEAR, DateTime.now[DateTime.DAY_OF_YEAR])
                showTimePicker()
            }
            1 -> {
                dialog?.dismiss()
                dateTime = dateTime.with(
                    DateTime.DAY_OF_YEAR,
                    DateTime.now[DateTime.DAY_OF_YEAR]
                ) - 1L.days()
                showTimePicker()
            }
            2 -> {
                dialog?.dismiss()
                showDatePicker()
            }
        }
    }

    fun show(date: DateTime) {
        dateTime = date

        val options = activity.resources.run {
            arrayOf(
                getString(R.string.time_today),
                getString(R.string.time_yesterday),
                getString(R.string.glucose_editor_date_pick)
            )
        }

        AlertDialog.Builder(activity)
            .setTitle(R.string.glucose_editor_time_dialog)
            .setItems(options, this)
            .show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            activity,
            R.style.AppTheme_DatePickerDialog,
            timePickerCallback,
            dateTime[DateTime.HOUR],
            dateTime[DateTime.MINUTE],
            true
        ).show()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            activity,
            R.style.AppTheme_DatePickerDialog,
            datePickerCallback,
            dateTime[DateTime.YEAR],
            dateTime[DateTime.MONTH],
            dateTime[DateTime.DAY]
        ).show()
    }
}
