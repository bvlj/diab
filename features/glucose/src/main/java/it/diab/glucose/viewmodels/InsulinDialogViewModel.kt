/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.diab.data.entities.Glucose
import it.diab.data.entities.Insulin
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.glucose.components.status.InsulinDialogInStatus
import it.diab.glucose.components.status.InsulinDialogOutStatus
import it.diab.glucose.util.InsulinSelector
import kotlinx.coroutines.launch

internal class InsulinDialogViewModel internal constructor(
    private val glucoseRepository: GlucoseRepository,
    private val insulinRepository: InsulinRepository
) : ViewModel() {

    private lateinit var insulins: List<Insulin>
    private lateinit var glucose: Glucose
    private var wantsBasal = false

    fun prepare(uid: Long, wantsBasal: Boolean, block: (InsulinDialogInStatus) -> Unit) {
        viewModelScope.launch {
            val status = runPrepare(uid, wantsBasal)
            block(status)
        }
    }

    suspend fun setInsulin(status: InsulinDialogOutStatus) {
        if (status.value <= 0f) {
            return
        }

        if (wantsBasal) {
            runSetBasal(status)
        } else {
            runSetInsulin(status)
        }
    }

    suspend fun removeInsulin() {
        if (wantsBasal) {
            runRemoveBasal()
        } else {
            runRemoveInsulin()
        }
    }

    @VisibleForTesting
    suspend fun runPrepare(uid: Long, wantsBasal: Boolean): InsulinDialogInStatus {
        this.wantsBasal = wantsBasal
        glucose = glucoseRepository.getById(uid)
        insulins = insulinRepository.getInsulins().filter { it.isBasal == wantsBasal }

        if (insulins.isEmpty()) {
            return InsulinDialogInStatus.Empty
        }

        val selector = InsulinSelector(glucose.timeFrame)
        val suggested =
            if (wantsBasal) selector.suggestBasal(insulins, glucose.insulinId1)
            else selector.suggestInsulin(insulins, glucose.insulinId0)
        val currentValue =
            if (wantsBasal) glucose.insulinValue1
            else glucose.insulinValue0

        return InsulinDialogInStatus.Edit(
            uid > 0L,
            suggested,
            insulins.map(Insulin::name),
            currentValue
        )
    }

    @VisibleForTesting
    suspend fun runSetInsulin(status: InsulinDialogOutStatus) {
        glucoseRepository.insert(glucose.apply {
            insulinId0 = insulins[status.selectedInsulin].uid
            insulinValue0 = status.value
        })
    }

    @VisibleForTesting
    suspend fun runSetBasal(status: InsulinDialogOutStatus) {
        glucoseRepository.insert(glucose.apply {
            insulinId1 = insulins[status.selectedInsulin].uid
            insulinValue1 = status.value
        })
    }

    @VisibleForTesting
    suspend fun runRemoveInsulin() {
        glucoseRepository.insert(glucose.apply {
            insulinId0 = -1L
            insulinValue0 = 0f
        })
    }

    @VisibleForTesting
    suspend fun runRemoveBasal() {
        glucoseRepository.insert(glucose.apply {
            insulinId1 = -1L
            insulinValue1 = 0f
        })
    }
}
