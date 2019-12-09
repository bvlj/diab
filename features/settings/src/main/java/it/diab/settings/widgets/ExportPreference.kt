/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.settings.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.app.AlertDialog
import androidx.preference.DialogPreference
import it.diab.settings.R
import it.diab.ui.util.extensions.getAttr

internal class ExportPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = context.getAttr(R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle),
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {
    private lateinit var callbacks: Callbacks

    override fun onClick() {
        val activity = callbacks.getActivity() ?: return

        AlertDialog.Builder(activity)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setPositiveButton(R.string.export_ask_positive) { _, _ ->
                callbacks.startExport()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    fun bind(callbacks: Callbacks) {
        this.callbacks = callbacks
    }

    interface Callbacks {

        /**
         * Get an activity in which display dialogs
         */
        fun getActivity(): Activity?

        /**
         * Export has been confirmed, proceed
         */
        fun startExport()
    }
}
