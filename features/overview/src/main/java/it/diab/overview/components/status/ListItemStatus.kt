/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
@file:JvmName("ListItemStatus")

package it.diab.overview.components.status

import it.diab.core.time.DateTime
import it.diab.data.entities.Insulin

internal data class GlucoseItemStatus(
    val uid: Long,
    val value: Int,
    val date: DateTime,
    val insulin: Insulin?,
    val insulinValue: Float,
    val basal: Insulin?,
    val basalValue: Float
)

internal data class HeaderItemStatus(
    val last: LastGlucose,
    val graphData: GraphData
)
