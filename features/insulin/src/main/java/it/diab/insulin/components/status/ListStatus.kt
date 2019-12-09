/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
@file:JvmName("ListStatus")

package it.diab.insulin.components.status

import androidx.annotation.StringRes
import androidx.paging.PagedList
import it.diab.core.arch.ViewStatus
import it.diab.data.entities.Insulin

internal data class ListStatus(
    val pagedList: PagedList<Insulin>
) : ViewStatus

internal data class ListItemStatus(
    val uid: Long,
    val name: String,
    @StringRes
    val timeFrameRes: Int,
    val isBasal: Boolean
) : ViewStatus
