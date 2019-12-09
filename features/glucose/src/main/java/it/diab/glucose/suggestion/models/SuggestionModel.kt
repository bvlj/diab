/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.suggestion.models

import android.content.res.Resources
import androidx.annotation.DrawableRes
import it.diab.glucose.suggestion.status.SuggestionStatus

abstract class SuggestionModel<T, S : SuggestionStatus>(protected val status: S) {

    /**
     * Check whether the suggestion has errors.
     * Used to display an error message in the UI
     *
     * @param value the suggestion
     *
     * @return whether there errors occurred
     */
    abstract fun validate(value: T): Boolean

    /**
     * Get the error message for the ui
     *
     * @param value the suggestion
     * @param res resources to fetch the string resource. May be null
     *
     * @return the message or null if the occurred error requires the view to be hidden
     */
    abstract fun getFailMessage(value: T, res: Resources? = null): String?

    /**
     * Get the final message for the ui
     *
     * @param value the suggestion
     * @param res resources to fetch the string resource. May be null
     *
     * @return the message shown to the user
     */
    abstract fun getSuccessMessage(value: T, res: Resources? = null): String

    /**
     * Callback for button click
     *
     * @param value the suggestion
     */
    abstract fun onSuggestionApply(value: T)

    /**
     * Whether this suggestion is valid.
     * If it's not valid the suggestion won't appear in the UI
     */
    abstract fun isValid(): Boolean

    /**
     * Get an icon that represents the suggestion
     */
    @get:DrawableRes
    abstract val icon: Int
}
