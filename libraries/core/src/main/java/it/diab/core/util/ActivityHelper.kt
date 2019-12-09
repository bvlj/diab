/*
 * Copyright 2018 Google, Inc.
 *           2019 Bevilacqua Joey
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
@file:JvmName("ActivityHelper")
package it.diab.core.util

import android.content.Intent

/*
 * Helpers to start activities in a modularized world.
 */

private const val PACKAGE_NAME = "it.diab"

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        PACKAGE_NAME,
        addressableActivity.className
    )
}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

object Activities {

    object Glucose {
        object Editor : AddressableActivity {
            override val className = "$PACKAGE_NAME.glucose.ui.GlucoseActivity"

            const val EXTRA_UID = "glucose_uid"
            const val EXTRA_INSULIN_BASAL = "glucose_insulin_basal"
        }
    }

    object Insulin : AddressableActivity {
        override val className = "$PACKAGE_NAME.insulin.ui.InsulinActivity"

        const val EXTRA_EDITOR_UID = "insulin_uid"
    }

    object Settings : AddressableActivity {
        override val className = "$PACKAGE_NAME.settings.ui.SettingsActivity"

        const val PREF_UI_STYLE = "pref_ui_style"
    }
}
