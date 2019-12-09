/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.export.utils.extensions

import org.dhatim.fastexcel.Color
import org.dhatim.fastexcel.Worksheet

/**
 * Set alternate background color for
 * the given sheet
 *
 * @param x number or columns to apply the background onto
 * @param y number or rows to apply the background onto
 */
internal fun Worksheet.setAlternateBackground(x: Int, y: Int) {
    range(0, 0, x, y)
        .style()
        .shadeAlternateRows(Color.GRAY2)
        .set()
}

/**
 * Insert data in the given sheet
 *
 * @param values pairs composed of [Any] (data) and [Pair] (x, y) coordinate in the sheet
 */
internal fun Worksheet.write(vararg values: Pair<Any, Pair<Int, Int>>) {
    values.forEach {
        value(it.second.first, it.second.second, it.first)
    }
}
