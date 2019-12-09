/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.suggestion.status

import it.diab.data.entities.TimeFrame

internal data class InsulinStatus(
    val timeFrame: TimeFrame,
    val hasInsulin: Boolean,
    val proposedInsulinUid: Long,
    val increaseByHalf: Boolean,
    val onSuggestionApplied: (value: Float, insulinUid: Long) -> Unit
) : SuggestionStatus
