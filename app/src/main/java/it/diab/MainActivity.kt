/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.diab.overview.fragments.OverviewFragment
import it.diab.util.ShortcutUtils

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, OverviewFragment())
            .commit()
        createShortcuts()
    }

    private fun createShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutUtils.setupShortcuts(this)
        }
    }
}
