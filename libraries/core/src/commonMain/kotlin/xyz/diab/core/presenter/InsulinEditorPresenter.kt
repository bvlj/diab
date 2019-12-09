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

import xyz.diab.core.model.Insulin
import xyz.diab.core.usecase.insulin.DeleteInsulinUseCaseProducer
import xyz.diab.core.usecase.insulin.GetInsulinUseCaseProducer
import xyz.diab.core.usecase.insulin.NameExistsInsulinUseCaseProducer
import xyz.diab.core.usecase.insulin.PutInsulinUseCaseProducer

class InsulinEditorPresenter internal constructor(
    private val getInsulinUseCaseProducer: GetInsulinUseCaseProducer,
    private val putInsulinUseCaseProducer: PutInsulinUseCaseProducer,
    private val deleteInsulinUseCaseProducer: DeleteInsulinUseCaseProducer,
    private val nameExistsInsulinUseCaseProducer: NameExistsInsulinUseCaseProducer
) {

    suspend fun getByName(itemName: String) =
        getInsulinUseCaseProducer(itemName)()

    suspend fun put(item: Insulin) =
        putInsulinUseCaseProducer(item)()

    suspend fun delete(item: Insulin) {
        deleteInsulinUseCaseProducer(item)()
    }

    suspend fun nameExists(proposedName: String) =
        nameExistsInsulinUseCaseProducer(proposedName)()
}
