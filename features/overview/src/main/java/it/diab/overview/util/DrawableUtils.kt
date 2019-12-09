/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

internal object DrawableUtils {

    fun createRoundDrawable(resources: Resources, size: Int, @ColorInt color: Int): Drawable {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val rect = RectF(Rect(0, 0, size, size))
        val paint = Paint().apply {
            isAntiAlias = true
            this.color = color
        }

        Canvas(bitmap).drawRoundRect(rect, size / 2f, size / 2f, paint)

        return RoundedBitmapDrawableFactory.create(resources, bitmap)
    }
}
