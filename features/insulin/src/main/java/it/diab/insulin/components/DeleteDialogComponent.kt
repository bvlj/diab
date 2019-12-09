/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import it.diab.core.arch.EventBusFactory
import it.diab.insulin.R
import it.diab.insulin.events.EditEvent
import kotlinx.coroutines.CoroutineScope

internal class DeleteDialogComponent(
    context: Context,
    scope: CoroutineScope,
    private val bus: EventBusFactory
) {

    private val dialog: AlertDialog
    @SuppressLint("InflateParams")
    private val dialogView = LayoutInflater.from(context)
        .inflate(R.layout.dialog_insulin_delete, null)
    private val valuesDeleteSwitch =
        dialogView.findViewById<Switch>(R.id.insulin_delete_dialog_values)
    private val dialogMsg =
        dialogView.findViewById<TextView>(R.id.insulin_delete_dialog_msg)

    init {
        bus.subscribe(EditEvent::class, scope) {
            when (it) {
                is EditEvent.IntentAskDelete -> showDeleteDialog(it.name)
            }
        }

        dialog = AlertDialog.Builder(context)
            .setTitle(R.string.insulin_delete_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.insulin_delete_positive) { _, _ ->
                bus.emit(
                    EditEvent::class,
                    EditEvent.IntentRequestDelete(valuesDeleteSwitch.isChecked)
                )
            }
            .create()
    }

    private fun showDeleteDialog(name: String) {
        dialogMsg.text = dialogView.resources.getString(R.string.insulin_delete_msg, name)
        dialog.show()
    }
}
