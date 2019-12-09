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

package xyz.diab.editor.bolus.viewmodel

import androidx.lifecycle.ViewModel
import xyz.diab.core.model.Bolus
import xyz.diab.core.presenter.BolusEditorPresenter

internal class BolusEditorViewModel(
    private val presenter: BolusEditorPresenter
) : ViewModel() {

    suspend fun getGlucose(id: Long) =
        presenter.getGlucose(id)

    suspend fun saveBolus(itemId: Long, bolus: Bolus, basal: Boolean) =
        presenter.saveBolus(itemId, bolus, basal)

    suspend fun getInsulinNames(basal: Boolean): Array<String> =
        presenter.getInsulinNames(basal)
}
