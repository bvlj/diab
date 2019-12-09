/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.overview.components.status

import androidx.paging.PagedList
import it.diab.core.arch.ViewStatus
import it.diab.data.entities.GlucoseWithInsulin

internal data class DataStatus(val pagedList: PagedList<GlucoseWithInsulin>) : ViewStatus
