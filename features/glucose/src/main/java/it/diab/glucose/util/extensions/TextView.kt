/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.util.extensions

import android.animation.ValueAnimator
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.widget.TextView
import androidx.core.content.ContextCompat
import it.diab.glucose.R
import it.diab.ui.util.extensions.getColorAttr

internal fun TextView.setIconErrorStatus(toError: Boolean) {
    val originalColor = ContextCompat.getColor(context, R.color.colorAccent)
    val errorColor = ContextCompat.getColor(context, R.color.action_dangerous)

    val animator = ValueAnimator.ofArgb(
        if (toError) originalColor else errorColor,
        if (toError) errorColor else originalColor
    )

    val drawable = compoundDrawables[0]

    animator.addUpdateListener { animation ->
        val animColor = animation.animatedValue as Int
        if (Build.VERSION.SDK_INT >= 29) {
            drawable.colorFilter = BlendModeColorFilter(animColor, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            drawable.setColorFilter(animColor, PorterDuff.Mode.SRC_ATOP)
        }
    }

    animator.start()
}

internal fun TextView.setTextErrorStatus(toError: Boolean) {
    val originalColor = context.getColorAttr(R.style.DiabTheme, android.R.attr.textColorPrimary)
    val errorColor = ContextCompat.getColor(context, R.color.action_dangerous)

    val animator = ValueAnimator.ofArgb(
        if (toError) originalColor else errorColor,
        if (toError) errorColor else originalColor
    )

    animator.addUpdateListener { animation -> setTextColor(animation.animatedValue as Int) }
    animator.start()
}
