/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import xyz.diab.editor.bolus.BolusEditorFragment
import xyz.diab.editor.glucose.GlucoseEditorFragment
import xyz.diab.editor.insulin.InsulinEditorFragment
import xyz.diab.insulin.InsulinManagerFragment
import xyz.diab.overview.OverviewFragment
import xyz.diab.roboto.events.EventBus
import xyz.diab.roboto.navigation.NavigationArguments
import xyz.diab.roboto.navigation.NavigationTarget
import xyz.diab.roboto.navigation.UiNavigator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bus = EventBus[this]
        UiNavigator(bus).addOnNavigationListener(lifecycleScope) {
            when (it) {
                is NavigationTarget.Back ->
                    navigateBack()
                is NavigationTarget.GlucoseEditor ->
                    navigateToGlucoseEditor(it.id)
                is NavigationTarget.BolusEditor ->
                    navigateToBolusEditor(it.glucoseId, it.basal)
                is NavigationTarget.InsulinEditor ->
                    navigateToInsulinEditor(it.name)
                is NavigationTarget.InsulinManager ->
                    navigateToInsulinManager()
            }
        }

        if (savedInstanceState == null) {
            initOverview()
        }
    }

    private fun initOverview() {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, OverviewFragment())
            .commit()
    }

    private fun navigateBack() {
        // supportFragmentManager.popBackStack()
        onBackPressed()
    }

    private fun navigateToGlucoseEditor(itemId: Long) {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, GlucoseEditorFragment().apply {
                arguments = bundleOf(NavigationArguments.ARG_ID to itemId)
            })
            .addToBackStack(TAG_GLUCOSE_EDITOR)
            .commit()
    }

    private fun navigateToBolusEditor(glucoseId: Long, basal: Boolean) {
        BolusEditorFragment().apply {
            arguments = bundleOf(
                NavigationArguments.ARG_ID to glucoseId,
                NavigationArguments.ARG_BASAL to basal
            )

            show(supportFragmentManager, TAG_BOLUS_EDITOR)
        }
    }

    private fun navigateToInsulinEditor(name: String?) {
        InsulinEditorFragment().apply {
            arguments = bundleOf(NavigationArguments.ARG_NAME to name)

            show(supportFragmentManager, TAG_INSULIN_EDITOR)
        }
    }

    private fun navigateToInsulinManager() {
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, InsulinManagerFragment())
            .addToBackStack(TAG_INSULIN_MANAGER)
            .commit()
    }

    companion object {
        private const val TAG_GLUCOSE_EDITOR = ":fragment:editor:glucose"
        private const val TAG_BOLUS_EDITOR = ":fragment:editor:bolus"
        private const val TAG_INSULIN_MANAGER = ":fragment:manager:insulin"
        private const val TAG_INSULIN_EDITOR = ":fragment:editor:insulin"
    }
}
