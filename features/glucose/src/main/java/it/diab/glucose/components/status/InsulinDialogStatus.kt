/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
@file:JvmName("InsulinDialogStatus")

package it.diab.glucose.components.status

import it.diab.core.arch.ViewStatus

internal sealed class InsulinDialogInStatus : ViewStatus {
    data class Edit(
        val isEditing: Boolean,
        val preferrableIndex: Int,
        val selectableItems: List<String>,
        val value: Float
    ) : InsulinDialogInStatus()

    object Empty : InsulinDialogInStatus()
}

internal data class InsulinDialogOutStatus(
    val selectedInsulin: Int,
    val value: Float
) : ViewStatus
