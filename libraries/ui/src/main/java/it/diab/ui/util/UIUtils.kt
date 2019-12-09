/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.ui.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import it.diab.core.util.SystemUtil
import it.diab.ui.R

object UIUtils {

    fun setStyleMode(value: String) {
        AppCompatDelegate.setDefaultNightMode(
            when (value) {
                "0" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                "2" -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    @SuppressLint("PrivateResource")
    fun setWhiteNavBarIfNeeded(context: Context, dialog: Dialog?) {
        if (dialog == null) {
            return
        }

        val isLight = context.resources.getBoolean(R.bool.is_light)
        if (!isLight) {
            return
        }

        val window = dialog.window ?: return
        val metrics = DisplayMetrics().apply {
            window.windowManager.defaultDisplay.getMetrics(this)
        }
        val bgDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.dim_foreground_material_light))
            alpha = 50
        }
        val navBarDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(ContextCompat.getColor(context, R.color.navigationBar_color))
        }

        val background = LayerDrawable(arrayOf(bgDrawable, navBarDrawable)).apply {
            setLayerInsetTop(1, metrics.heightPixels)
        }

        window.setBackgroundDrawable(background)
    }

    fun supportsAutoStyleMode() = when {
        Build.VERSION.SDK_INT >= 28 -> true
        Build.VERSION.SDK_INT == 27 ->
            SystemUtil.getProp("ro.lineage.build.version.plat.sdk", "") == "9"
        else -> false
    }
}
