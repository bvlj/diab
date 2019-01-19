/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.util.extensions

fun <T> List<T>.forEachWithIndex(block: (Int, T) -> Unit) {
    var i = 0
    while (i < size) {
        block(i, get(i))
        i++
    }
}