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

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.diab.core.presenter.PresenterFactory
import xyz.diab.roboto.data.RobotoRepositoryFactory

internal class BolusEditorViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val glucoseRepo = RobotoRepositoryFactory.provideGlucoseRepo(context)
    private val insulinRepo = RobotoRepositoryFactory.provideInsulinRepo(context)

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = BolusEditorViewModel(
        PresenterFactory.provideBolusEditorPresenter(glucoseRepo, insulinRepo)
    ) as T
}
