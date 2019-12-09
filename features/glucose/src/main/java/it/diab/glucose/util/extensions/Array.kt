/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.util.extensions

internal fun <T> Array<T>.forEachUntilTrueIndexed(checker: (Int, T) -> Boolean) {
    for ((index, item) in withIndex()) {
        if (checker(index, item)) {
            break
        }
    }
}
