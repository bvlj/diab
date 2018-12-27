package it.diab.viewmodels.glucose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.diab.db.repositories.GlucoseRepository
import it.diab.db.repositories.InsulinRepository

class EditorViewModelFactory(
        private val glucoseRepository: GlucoseRepository,
        private val insulinRepository: InsulinRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
            EditorViewModel(glucoseRepository, insulinRepository) as T
 }