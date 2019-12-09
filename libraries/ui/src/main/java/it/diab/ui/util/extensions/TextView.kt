/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.ui.util.extensions

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat

fun AppCompatTextView.setPreText(text: CharSequence) {
    setTextFuture(
        PrecomputedTextCompat.getTextFuture(
            text,
            textMetricsParamsCompat,
            null
        )
    )
}
