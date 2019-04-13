/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.holders

import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineScope
import java.util.Date

interface GlucoseHolderCallbacks {

    /**
     * Fetch a String that for the header
     *
     * @param date date to be put in the string
     * @param onFetch callback for fetch completion
     */
    fun fetchHeaderText(
        date: Date,
        onFetch: (String, CoroutineScope) -> Unit
    )

    /**
     * Fetch a String that represents hours of a given [Date]
     *
     * @param date date to be put in the string
     * @param onFetch callback for fetch completion
     */
    fun fetchHourText(
        date: Date,
        onFetch: (String, CoroutineScope) -> Unit
    )

    /**
     * Get the indicator drawable for
     * a given glucose value
     */
    fun getIndicator(value: Int): Drawable?

    /**
     * Get the name of an insulin
     *
     * @param uid uid of the insulin
     */
    fun getInsulinName(uid: Long): String

    /**
     * OnClick event callback
     */
    fun onClick(uid: Long)

    /**
     * Whether the header should be shown
     *
     * @param position position of the glucose in the list
     */
    fun shouldInsertHeader(position: Int): Boolean
}