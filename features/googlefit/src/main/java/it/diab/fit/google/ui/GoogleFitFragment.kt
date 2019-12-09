/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.fit.google.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.diab.core.util.extensions.bus
import it.diab.fit.google.R
import it.diab.fit.google.components.GoogleFitComponent
import it.diab.fit.google.events.GoogleFitEvents
import it.diab.fit.google.util.GoogleFitManager
import it.diab.fit.google.viewmodels.GoogleFitViewModel
import it.diab.fit.google.viewmodels.GoogleFitViewModelFactory

class GoogleFitFragment : Fragment() {
    private lateinit var viewModel: GoogleFitViewModel
    private lateinit var fitManager: GoogleFitManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val factory = GoogleFitViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[GoogleFitViewModel::class.java]

        val activity = activity ?: return
        fitManager = GoogleFitManager(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_fit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GoogleFitComponent(view, viewModel.viewModelScope, bus)

        bus.subscribe(GoogleFitEvents::class, viewModel.viewModelScope) {
            when (it) {
                is GoogleFitEvents.DeleteUserDataEvent -> onDeleteEvent()
                is GoogleFitEvents.DisconnectEvent -> onDisconnectEvent()
                is GoogleFitEvents.ConnectEvent -> onConnectEvent()
            }
        }

        bus.emit(
            GoogleFitEvents::class,
            GoogleFitEvents.SetupEvent(fitManager.isConnected())
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GOOGLE_FIT_REQUEST_CODE -> bus.emit(
                GoogleFitEvents::class,
                GoogleFitEvents.OnConnectedEvent(resultCode == Activity.RESULT_OK)
            )
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onDeleteEvent() {
        fitManager.deleteAllData { success ->
            bus.emit(GoogleFitEvents::class, GoogleFitEvents.OnDataDeletedEvent(success))
        }
    }

    private fun onDisconnectEvent() {
        fitManager.disconnect { success ->
            bus.emit(GoogleFitEvents::class, GoogleFitEvents.OnDisconnectedEvent(success))
        }
    }

    private fun onConnectEvent() {
        fitManager.connect(GOOGLE_FIT_REQUEST_CODE)
    }

    companion object {
        private const val GOOGLE_FIT_REQUEST_CODE = 281
    }
}
