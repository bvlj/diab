/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import it.diab.insulin.R

class InsulinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_insulin)

        findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_toolbar_back)
            setNavigationOnClickListener { onBackPressed() }
        }
    }
}
