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

package xyz.diab.core.presenter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.diab.core.model.Bolus
import xyz.diab.core.model.Insulin
import xyz.diab.core.usecase.glucose.GetGlucoseUseCaseProducer
import xyz.diab.core.usecase.glucose.PutGlucoseUseCaseProducer
import xyz.diab.core.usecase.insulin.GetAllInsulinUseCase

class BolusEditorPresenter internal constructor(
    private val getInsulinUseCase: GetAllInsulinUseCase,
    private val getGlucoseUseCaseProducer: GetGlucoseUseCaseProducer,
    private val putGlucoseUseCaseProducer: PutGlucoseUseCaseProducer
) {

    suspend fun getGlucose(itemId: Long) =
        getGlucoseUseCaseProducer(itemId)()

    suspend fun saveBolus(itemId: Long, bolus: Bolus, basal: Boolean): Long {
        val item = getGlucose(itemId)

        val toSave = if (basal) item.copy(basalBolus = bolus) else item.copy(bolus = bolus)
        return putGlucoseUseCaseProducer(toSave)()
    }

    suspend fun getInsulinNames(basal: Boolean): Array<String> = withContext(Dispatchers.Default) {
        getInsulinUseCase()
            .filter { it.basal == basal }
            .map(Insulin::name)
            .toTypedArray()
    }
}
