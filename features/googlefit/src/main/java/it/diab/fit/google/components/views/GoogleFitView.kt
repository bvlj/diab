/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.fit.google.components.views

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import it.diab.core.arch.EventBusFactory
import it.diab.core.arch.UiView
import it.diab.core.arch.ViewStatus
import it.diab.fit.google.R
import it.diab.fit.google.components.status.GoogleFitStatus
import it.diab.fit.google.events.GoogleFitEvents

class GoogleFitView(
    container: View,
    private val bus: EventBusFactory
) : UiView<GoogleFitStatus, ViewStatus>(container) {

    private val headerText: TextView =
        container.findViewById(R.id.fit_header_text)

    private val connectButton: MaterialButton =
        container.findViewById(R.id.fit_connect_button)

    private val disconnectButton: MaterialButton =
        container.findViewById(R.id.fit_disconnect_button)

    private val deleteAllButton: MaterialButton =
        container.findViewById(R.id.fit_delete_all_button)

    private var isConnected = false

    init {
        connectButton.setOnClickListener {
            bus.emit(GoogleFitEvents::class, GoogleFitEvents.ConnectEvent)
        }

        disconnectButton.setOnClickListener { confirmDisconnect() }

        deleteAllButton.setOnClickListener { confirmDelete() }
    }

    override fun setStatus(status: GoogleFitStatus) {
        when (status) {
            is GoogleFitStatus.DataDeleted -> onDataDeleted(status.success)
            is GoogleFitStatus.Connected -> setupConnected(status.success)
            is GoogleFitStatus.Disconnected -> setupDisconnected(status.success)
        }
    }

    private fun setupConnected(success: Boolean) {
        if (!success) {
            showSnack(R.string.fit_login_error)
            return
        }

        headerText.setText(R.string.fit_status_connected)
        connectButton.visibility = View.GONE
        disconnectButton.isEnabled = true
        deleteAllButton.isEnabled = true
        isConnected = true
    }

    private fun setupDisconnected(success: Boolean) {
        if (!success) {
            showSnack(R.string.fit_login_error)
            return
        }

        headerText.setText(R.string.fit_status_prompt)
        connectButton.visibility = View.VISIBLE
        disconnectButton.isEnabled = false
        deleteAllButton.isEnabled = false

        if (isConnected) {
            showSnack(R.string.fit_disconnect_success)
        }
        isConnected = false
    }

    private fun onDataDeleted(success: Boolean) {
        showSnack(if (success) R.string.fit_delete_success else R.string.fit_delete_failure)
    }

    private fun confirmDisconnect() {
        AlertDialog.Builder(container.context)
            .setTitle(R.string.fit_disconnect_confim_title)
            .setMessage(R.string.fit_disconnect_confim_message)
            .setPositiveButton(R.string.fit_disconnect_confim_positive) { _, _ ->
                bus.emit(GoogleFitEvents::class, GoogleFitEvents.DisconnectEvent)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun confirmDelete() {
        AlertDialog.Builder(container.context)
            .setTitle(R.string.fit_delete_confirm_title)
            .setMessage(R.string.fit_delete_confirm_message)
            .setPositiveButton(R.string.fit_delete_confim_positive) { _, _ ->
                bus.emit(GoogleFitEvents::class, GoogleFitEvents.DeleteUserDataEvent)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showSnack(@StringRes message: Int) {
        Snackbar.make(
            container,
            container.context.getString(message),
            BaseTransientBottomBar.LENGTH_LONG
        ).show()
    }
}
