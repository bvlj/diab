/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.settings.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.preference.ListPreference
import it.diab.settings.R
import it.diab.ui.util.UIUtils
import it.diab.ui.util.extensions.getAttr

internal class StylePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = context.getAttr(R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle),
    defStyleRes: Int = 0
) : ListPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        setupEntries()
        setupOnChange()
    }

    private fun setupEntries() {
        val supportsAuto = UIUtils.supportsAutoStyleMode()

        entries = if (supportsAuto) {
            arrayOf(
                context.getString(R.string.settings_ui_theme_system),
                context.getString(R.string.settings_ui_theme_light),
                context.getString(R.string.settings_ui_theme_dark)
            )
        } else {
            arrayOf(
                context.getString(R.string.settings_ui_theme_light),
                context.getString(R.string.settings_ui_theme_dark)
            )
        }

        entryValues = if (supportsAuto) {
            arrayOf("0", "1", "2")
        } else {
            arrayOf("1", "2")
        }
    }

    private fun setupOnChange() {
        setOnPreferenceChangeListener { _, newValue ->
            UIUtils.setStyleMode(newValue.toString())
            true
        }
    }
}
