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

package xyz.diab.roboto.navigation

@Suppress("KDocUnresolvedReference")
object NavigationArguments {

    /**
     * Represents the id of a [xyz.diab.core.model.Glucose] object
     *
     * To be used in [xyz.diab.editor.glucose.GlucoseEditorFragment]
     * and [xyz.diab.editor.bolus.BolusEditorFragment]
     *
     * Type: [Long]
     */
    const val ARG_ID = ":diab:id"

    /**
     * Represents whether we're dealing with the basal or
     * non-basal bolus of a [xyz.diab.core.model.Glucose]
     *
     * To be used in [xyz.diab.editor.bolus.BolusEditorFragment]
     *
     * Type: [Boolean]
     */
    const val ARG_BASAL = ":diab:basal"

    /**
     * Represents the name of a [xyz.diab.core.model.Insulin] object
     *
     * To be used in [xyz.diab.editor.insulin.InsulinEditorFragment]
     *
     * Type: [String]
     */
    const val ARG_NAME = ":diab:name"
}
