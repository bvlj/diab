/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.export.utils.extensions

import kotlin.math.ceil

internal fun ByteArray.splitBy(chunkSize: Int): Array<ByteArray> {
    val splitLen = ceil(size / chunkSize.toDouble()).toInt()
    return Array(splitLen) { position ->
        val start = chunkSize * position
        var end = start + chunkSize
        // Avoid out of bounds
        if (end >= size) {
            end = size - 1
        }

        copyOfRange(start, end)
    }
}
