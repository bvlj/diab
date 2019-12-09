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

package xyz.diab.core.usecase.insulin

import xyz.diab.core.data.insulin.InsulinRepository
import xyz.diab.core.model.Insulin
import xyz.diab.core.usecase.SuspendUseCase

class DeleteInsulinUseCase internal constructor(
    private val repo: InsulinRepository,
    private val insulin: Insulin
) : SuspendUseCase<Unit> {

    override suspend operator fun invoke() =
        repo.delete(insulin)
}
