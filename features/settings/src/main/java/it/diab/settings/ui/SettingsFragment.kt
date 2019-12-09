/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.settings.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import it.diab.core.util.Activities
import it.diab.core.util.intentTo
import it.diab.export.utils.SecureFilePickerHelper
import it.diab.settings.R
import it.diab.settings.widgets.ExportPreference

internal class SettingsFragment : PreferenceFragmentCompat(), SecureFilePickerHelper.Callbacks {
    private lateinit var prefs: SharedPreferences
    private lateinit var secureFilePicker: SecureFilePickerHelper

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        prefs = preferenceManager.sharedPreferences
        secureFilePicker = SecureFilePickerHelper(this, this)

        val exportXlsx = findPreference("pref_export_xlsx") as ExportPreference?
        exportXlsx?.bind(object : ExportPreference.Callbacks {
            override fun getActivity() = activity
            override fun startExport() { secureFilePicker.authenticate(SecureFilePickerHelper.XLSX) }
        })

        val manageInsulins: Preference? = findPreference("pref_link_insulin")
        manageInsulins?.setOnPreferenceClickListener { openInsulinView() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        secureFilePicker.onResult(requestCode, resultCode, data)
    }

    override fun onAuthentication(category: Int, result: SecureFilePickerHelper.AuthResult) {
        if (result == SecureFilePickerHelper.AuthResult.FAILURE) {
            Toast.makeText(context, R.string.export_failed_auth, Toast.LENGTH_LONG).show()
            return
        }

        secureFilePicker.pickDestination(category)
    }

    private fun openInsulinView(): Boolean {
        startActivity(intentTo(Activities.Insulin))
        return true
    }
}
