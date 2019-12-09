/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.settings.ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import it.diab.core.time.DateTime
import it.diab.core.util.extensions.get
import it.diab.data.plugin.PluginManager
import it.diab.export.utils.SecureFilePickerHelper
import it.diab.settings.R
import it.diab.settings.widgets.ExportPreference

internal class PluginPrefsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener,
    SecureFilePickerHelper.Callbacks {

    private lateinit var prefs: SharedPreferences
    private lateinit var pluginManager: PluginManager
    private lateinit var secureFilePicker: SecureFilePickerHelper

    private var exportPref: ExportPreference? = null
    private var managerPref: Preference? = null
    private var removerPref: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs_plugin, rootKey)

        prefs = preferenceManager.sharedPreferences
        pluginManager = PluginManager(requireContext())
        secureFilePicker = SecureFilePickerHelper(this, this)

        exportPref = findPreference("pref_plugin_export") as ExportPreference?
        managerPref = findPreference("pref_plugin_manager")
        removerPref = findPreference("pref_plugin_remover")

        managerPref?.setOnPreferenceClickListener { fetchPluginFile() }
        removerPref?.setOnPreferenceClickListener { askPluginRemoval() }

        exportPref?.bind(object : ExportPreference.Callbacks {
            override fun getActivity() = activity
            override fun startExport() { secureFilePicker.authenticate(SecureFilePickerHelper.ML) }
        })

        prefs.registerOnSharedPreferenceChangeListener(this)
        updatePluginPrefs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_PLUGIN) {
            onPluginSelected(resultCode, data)
            return
        }

        secureFilePicker.onResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            PluginManager.LAST_UPDATE -> updatePluginPrefs()
        }
    }

    override fun onAuthentication(category: Int, result: SecureFilePickerHelper.AuthResult) {
        if (result == SecureFilePickerHelper.AuthResult.FAILURE) {
            Toast.makeText(context, R.string.export_failed_auth, Toast.LENGTH_LONG).show()
            return
        }

        secureFilePicker.pickDestination(category)
    }

    private fun fetchPluginFile(): Boolean {
        startActivityForResult(pluginManager.getPickerIntent(), REQUEST_SELECT_PLUGIN)
        return true
    }

    private fun askPluginRemoval(): Boolean {
        val activity = activity ?: return false

        AlertDialog.Builder(activity)
            .setTitle(R.string.settings_insulin_plugin_remove)
            .setMessage(R.string.settings_insulin_plugin_remove_confirmation)
            .setPositiveButton(R.string.settings_insulin_plugin_remove_confirmation_positive) { _, _ -> pluginManager.uninstall() }
            .setNegativeButton(android.R.string.no, null)
            .show()

        return true
    }

    private fun updatePluginPrefs() {
        val lastUpdated = prefs[PluginManager.LAST_UPDATE, 0L]

        if (lastUpdated == 0L) {
            managerPref?.setSummary(R.string.settings_insulin_plugin_manage_summary_new)
            removerPref?.isVisible = false
        } else {
            managerPref?.summary = getString(
                R.string.settings_insulin_plugin_manage_summary_installed,
                DateTime(lastUpdated).format("yyyy-MM-dd HH:mm")
            )
            removerPref?.isVisible = true
        }
    }

    private fun onPluginSelected(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return
        }

        val context = context ?: return
        val uri = data.data ?: return
        val iStream = context.contentResolver.openInputStream(uri) ?: return

        pluginManager.install(iStream)
        Toast.makeText(context, R.string.settings_insulin_plugin_installing, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        private const val REQUEST_SELECT_PLUGIN = 393
    }
}
