/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
@file:JvmName("EditableStatus")

package it.diab.insulin.components.status

import it.diab.core.arch.ViewStatus

internal data class EditableInStatus(
    val isEdit: Boolean,
    val name: String,
    val timeFrameIndex: Int,
    val timeFrameOptions: List<Int>,
    val hasHalfUnits: Boolean,
    val isBasal: Boolean
) : ViewStatus

internal data class EditableOutStatus(
    val name: String = "",
    val timeFrameIndex: Int = 0,
    val hasHalfUnits: Boolean = false,
    val isBasal: Boolean = false
) : ViewStatus
