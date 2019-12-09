/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.override

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * Base class for Fitness services integration.
 * Create a class that extends this one and
 * update the R.string.config_class_fit_handler
 */
open class BaseFitHandler {

    /**
     * Override this to true to enable the
     * fitness integration
     */
    open val isEnabled = false

    /**
     * Whether fitness integration is allowed
     */
    open fun hasFit(context: Context) = false

    /**
     * Get the fragment for managing fitness
     */
    open fun getFragment(): Fragment {
        throw UnsupportedOperationException("No fragment available")
    }

    /**
     * Upload the glucose data to the fitness service
     */
    open fun upload(
        context: Context,
        item: Any,
        isNew: Boolean,
        onCompletion: (Boolean) -> Unit
    ) = Unit
}
