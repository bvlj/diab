/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.diab.core.util.Activities
import it.diab.glucose.fragments.EditorFragment

class GlucoseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uid = intent.getLongExtra(Activities.Glucose.Editor.EXTRA_UID, -1L)
        val detailFragment = EditorFragment().apply {
            arguments = Bundle().apply {
                putLong(Activities.Glucose.Editor.EXTRA_UID, uid)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, detailFragment)
            .commit()
    }
}
