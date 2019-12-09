/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import it.diab.settings.R
import it.diab.settings.widgets.FitPreference

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_toolbar_back)
            setNavigationOnClickListener { onBackPressed() }
        }

        supportFragmentManager.addOnBackStackChangedListener(this::updateTitle)

        setFragment(SettingsFragment(), KEY_MAIN)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        val args = pref.extras
        val fragment = when (pref.key) {
            KEY_FIT -> (pref as? FitPreference)?.getFitFragment()
            KEY_PLUGIN -> PluginPrefsFragment()
            else -> null
        } ?: return false

        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        setFragment(fragment, pref.key)
        return true
    }

    private fun setFragment(fragment: Fragment, name: String?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, fragment)
            .addToBackStack(name)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun updateTitle() {
        val lastIndex = supportFragmentManager.backStackEntryCount - 1
        if (lastIndex < 0) {
            finish()
            return
        }

        val top = supportFragmentManager.getBackStackEntryAt(lastIndex)
        supportActionBar?.title = when (top.name) {
            KEY_MAIN -> getString(R.string.settings_name)
            KEY_FIT -> getString(R.string.settings_fit_title)
            KEY_PLUGIN -> getString(R.string.settings_insulin_plugin_title)
            else -> "???"
        }
    }

    companion object {
        private const val KEY_MAIN = "main"
        private const val KEY_FIT = "pref_link_fit"
        private const val KEY_PLUGIN = "pref_link_plugin"
    }
}
