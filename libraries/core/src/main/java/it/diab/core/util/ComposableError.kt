/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.util

class ComposableError {
    @Volatile
    private var error = 0

    operator fun plusAssign(value: Int) {
        error = error or value
    }

    operator fun minusAssign(value: Int) {
        error = error and value.inv()
    }

    operator fun contains(value: Int) = error and value != 0

    fun hasAny() = error != 0

    override fun toString() = error.toString()
}
