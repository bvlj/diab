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

package xyz.diab.core.usecase

import xyz.diab.core.data.glucose.GlucoseRepository
import xyz.diab.core.data.insulin.InsulinRepository
import xyz.diab.core.usecase.glucose.DeleteGlucoseUseCase
import xyz.diab.core.usecase.glucose.DeleteGlucoseUseCaseProducer
import xyz.diab.core.usecase.glucose.GetAllGlucoseUseCase
import xyz.diab.core.usecase.glucose.GetFlowGlucoseUseCase
import xyz.diab.core.usecase.glucose.GetGlucoseUseCase
import xyz.diab.core.usecase.glucose.GetGlucoseUseCaseProducer
import xyz.diab.core.usecase.glucose.GetInTimeRangeGlucoseUseCase
import xyz.diab.core.usecase.glucose.GetInTimeRangeGlucoseUseCaseProducer
import xyz.diab.core.usecase.glucose.PutGlucoseUseCase
import xyz.diab.core.usecase.glucose.PutGlucoseUseCaseProducer
import xyz.diab.core.usecase.insulin.DeleteInsulinUseCase
import xyz.diab.core.usecase.insulin.DeleteInsulinUseCaseProducer
import xyz.diab.core.usecase.insulin.GetAllInsulinUseCase
import xyz.diab.core.usecase.insulin.GetFlowInsulinUseCase
import xyz.diab.core.usecase.insulin.GetInsulinUseCase
import xyz.diab.core.usecase.insulin.GetInsulinUseCaseProducer
import xyz.diab.core.usecase.insulin.NameExistsInsulinUseCase
import xyz.diab.core.usecase.insulin.NameExistsInsulinUseCaseProducer
import xyz.diab.core.usecase.insulin.PutInsulinUseCase
import xyz.diab.core.usecase.insulin.PutInsulinUseCaseProducer

object UseCaseFactory {

    /* Glucose */

    fun provideDeleteGlucoseUseCaseProducer(
        repo: GlucoseRepository
    ): DeleteGlucoseUseCaseProducer = {
        DeleteGlucoseUseCase(repo, it)
    }

    fun provideGetAllGlucoseUseCase(
        repo: GlucoseRepository
    ) = GetAllGlucoseUseCase(repo)

    fun provideGetFlowGlucoseUseCase(
        repo: GlucoseRepository
    ) = GetFlowGlucoseUseCase(repo)

    fun provideGetGlucoseUseCaseProducer(
        repo: GlucoseRepository
    ): GetGlucoseUseCaseProducer = {
        GetGlucoseUseCase(repo, it)
    }

    fun provideGetInTimeRangeGlucoseUseCaseProducer(
        repo: GlucoseRepository
    ): GetInTimeRangeGlucoseUseCaseProducer = {
        GetInTimeRangeGlucoseUseCase(repo, it)
    }

    fun providePutGlucoseUseCaseProducer(
        repo: GlucoseRepository
    ): PutGlucoseUseCaseProducer = {
        PutGlucoseUseCase(repo, it)
    }

    /* Insulin */

    fun provideDeleteInsulinUseCaseProducer(
        repo: InsulinRepository
    ): DeleteInsulinUseCaseProducer = {
        DeleteInsulinUseCase(repo, it)
    }

    fun provideGetAllInsulinUseCase(
        repo: InsulinRepository
    ) = GetAllInsulinUseCase(repo)

    fun provideGetFlowInsulinUseCase(
        repo: InsulinRepository
    ) = GetFlowInsulinUseCase(repo)

    fun provideGetInsulinUseCaseProducer(
        repo: InsulinRepository
    ): GetInsulinUseCaseProducer = {
        GetInsulinUseCase(repo, it)
    }

    fun provideNameExistsInsulinUseCaseProducer(
        repo: InsulinRepository
    ): NameExistsInsulinUseCaseProducer = {
        NameExistsInsulinUseCase(repo, it)
    }

    fun providePutInsulinUseCaseProducer(
        repo: InsulinRepository
    ): PutInsulinUseCaseProducer = {
        PutInsulinUseCase(repo, it)
    }
}
