/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.fit.google.events

import it.diab.core.arch.ComponentEvent

internal sealed class GoogleFitEvents : ComponentEvent {

    data class SetupEvent(val isConnected: Boolean) : GoogleFitEvents()

    object ConnectEvent : GoogleFitEvents()

    object DisconnectEvent : GoogleFitEvents()

    object DeleteUserDataEvent : GoogleFitEvents()

    data class OnConnectedEvent(val success: Boolean) : GoogleFitEvents()

    data class OnDisconnectedEvent(val success: Boolean) : GoogleFitEvents()

    data class OnDataDeletedEvent(val success: Boolean) : GoogleFitEvents()
}
