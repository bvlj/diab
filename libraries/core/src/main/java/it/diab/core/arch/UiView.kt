/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.arch

import android.view.View

/**
 * Manages one or more [View]s.
 *
 * This component can only be aware of a [ViewStatus] that
 * holds the information used to set up the inner views.
 *
 * If the component has views that store information
 * that we want to retrieve at a certain point
 * (e.g. when a "Send" button is pressed we read the
 * message from the text field), we can return a [ViewStatus]
 * so that the component can later use it in its business logic.
 */
@Suppress("unused")
abstract class UiView<in I : ViewStatus, out O : ViewStatus>(protected val container: View) {

    open fun setStatus(status: I) = Unit

    open fun getStatus(): O {
        throw UnsupportedOperationException("This UiView does not return any ViewStatus")
    }
}
