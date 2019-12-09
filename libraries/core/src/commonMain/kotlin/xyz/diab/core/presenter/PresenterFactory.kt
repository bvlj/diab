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

import xyz.diab.core.data.glucose.GlucoseRepository
import xyz.diab.core.data.insulin.InsulinRepository
import xyz.diab.core.usecase.UseCaseFactory

object PresenterFactory {

    fun provideBolusEditorPresenter(
        glucoseRepo: GlucoseRepository,
        insulinRepo: InsulinRepository
    ) = BolusEditorPresenter(
        UseCaseFactory.provideGetAllInsulinUseCase(insulinRepo),
        UseCaseFactory.provideGetGlucoseUseCaseProducer(glucoseRepo),
        UseCaseFactory.providePutGlucoseUseCaseProducer(glucoseRepo)
    )

    fun provideGlucoseEditorPresenter(
        repo: GlucoseRepository
    ) = GlucoseEditorPresenter(
        UseCaseFactory.provideGetGlucoseUseCaseProducer(repo),
        UseCaseFactory.providePutGlucoseUseCaseProducer(repo)
    )

    fun provideInsulinEditorPresenter(
        repo: InsulinRepository
    ) = InsulinEditorPresenter(
        UseCaseFactory.provideGetInsulinUseCaseProducer(repo),
        UseCaseFactory.providePutInsulinUseCaseProducer(repo),
        UseCaseFactory.provideDeleteInsulinUseCaseProducer(repo),
        UseCaseFactory.provideNameExistsInsulinUseCaseProducer(repo)
    )

    fun provideOverviewPresenter(
        repo: GlucoseRepository
    ) = OverviewPresenter(
        UseCaseFactory.provideGetFlowGlucoseUseCase(repo),
        UseCaseFactory.providePutGlucoseUseCaseProducer(repo),
        UseCaseFactory.provideGetGlucoseUseCaseProducer(repo),
        UseCaseFactory.provideDeleteGlucoseUseCaseProducer(repo)
    )
}
