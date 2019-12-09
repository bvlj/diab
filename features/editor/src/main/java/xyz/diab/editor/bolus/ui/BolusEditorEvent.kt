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

package xyz.diab.editor.bolus.ui

import xyz.diab.core.model.Bolus
import xyz.diab.roboto.events.Event

internal sealed class BolusEditorEvent : Event {

    @Suppress("ArrayInDataClass")
    data class Setup(
        val glucoseId: Long,
        val bolus: Bolus,
        val basal: Boolean,
        val names: Array<String>,
        val selection: Int
    ) : BolusEditorEvent()

    data class Save(
        val glucoseId: Long,
        val bolus: Bolus,
        val basal: Boolean
    ) : BolusEditorEvent()

    object NavigateToInsulinManager : BolusEditorEvent()
}
