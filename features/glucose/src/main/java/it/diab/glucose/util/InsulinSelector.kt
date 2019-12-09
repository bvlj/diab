/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.util

import it.diab.data.entities.Insulin
import it.diab.data.entities.TimeFrame

internal class InsulinSelector(private val timeFrame: TimeFrame) {

    fun suggestInsulin(list: Collection<Insulin>, currentId: Long) =
        suggest(list, currentId, false)

    fun suggestBasal(list: Collection<Insulin>, currentId: Long) =
        suggest(list, currentId, true)

    private fun suggest(list: Collection<Insulin>, currentId: Long, isBasal: Boolean): Int {
        var suggested = -1
        list.forEachIndexed { i, insulin ->
            if (insulin.isBasal == isBasal) {
                if (suggested == -1 || insulin.uid == currentId || insulin.timeFrame == timeFrame) {
                    suggested = i
                }
            }
        }

        return suggested
    }
}
