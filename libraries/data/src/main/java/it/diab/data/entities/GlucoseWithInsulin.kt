/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.entities

import androidx.annotation.VisibleForTesting
import androidx.room.Embedded
import androidx.room.Relation

class GlucoseWithInsulin {

    @Embedded
    lateinit var glucose: Glucose

    /*
     * Do not use `_insulin`, use `insulin`
     *
     * Do not use `_basal`, use `basal`
     *
     * Relation does not support single items
     */

    @Relation(parentColumn = "insulinId0", entityColumn = "uid", entity = Insulin::class)
    @VisibleForTesting
    var _insulin: List<Insulin> = emptyList()

    @Relation(parentColumn = "insulinId1", entityColumn = "uid", entity = Insulin::class)
    @VisibleForTesting
    var _basal: List<Insulin> = emptyList()

    val insulin: Insulin?
        get() = _insulin.firstOrNull()

    val basal: Insulin?
        get() = _basal.firstOrNull()
}
