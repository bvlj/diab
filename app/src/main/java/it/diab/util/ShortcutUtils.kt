/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import it.diab.R
import it.diab.core.util.Activities
import it.diab.core.util.extensions.get
import it.diab.core.util.extensions.set
import it.diab.core.util.intentTo

@RequiresApi(26)
object ShortcutUtils {
    private const val SHORTCUTS_VERSION = 2
    private const val KEY_SHORTCUTS = "pref_home_shortcuts"
    private val SHORTCUTS = arrayOf(this::buildAddGlucoseShortcut)

    /**
     * Create or update the shortcuts if needed
     *
     * @param context used to fetch services and check SharedPreferences
     */
    fun setupShortcuts(context: Context) {
        if (areShortcutsOk(context)) {
            return
        }

        // Try to get the ShortcutManager. If it's not supported (eg. Go devices), do nothing
        val manager = context.getSystemService(ShortcutManager::class.java) ?: return

        manager.removeAllDynamicShortcuts()
        SHORTCUTS.forEach {
            manager.addDynamicShortcuts(listOf(it(context)))
        }

        updateShortcutVersion(context)
    }

    private fun areShortcutsOk(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs[KEY_SHORTCUTS, SHORTCUTS_VERSION - 1] >= SHORTCUTS_VERSION
    }

    private fun updateShortcutVersion(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs[KEY_SHORTCUTS] = SHORTCUTS_VERSION
    }

    private fun buildAddGlucoseShortcut(context: Context): ShortcutInfo {
        val title = context.getString(R.string.app_shortcut_add_glucose)
        val icon = getShortcutIcon(context, R.drawable.ic_shortcut_add_glucose)
        val intent = intentTo(Activities.Glucose.Editor).apply {
            action = Intent.ACTION_VIEW
        }

        return ShortcutInfo.Builder(context, title)
            .setIcon(Icon.createWithAdaptiveBitmap(icon))
            .setShortLabel(title)
            .setLongLabel(title)
            .setIntent(intent)
            .build()
    }

    private fun getShortcutIcon(context: Context, @DrawableRes icon: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, icon)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        requireNotNull(drawable) { "Could not get a valid drawable from argument" }

        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bm)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bm
    }
}
