/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository

internal class EditorViewModelFactory(
    private val glucoseRepo: GlucoseRepository,
    private val insulinRepo: InsulinRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditorViewModel(glucoseRepo, insulinRepo) as T
    }
}
