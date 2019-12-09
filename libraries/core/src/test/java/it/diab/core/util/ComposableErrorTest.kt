/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.util

import org.junit.Assert
import org.junit.Test

class ComposableErrorTest {

    @Test
    fun contain() {
        val error = ComposableError()

        error += ERROR_A
        Assert.assertTrue(ERROR_A in error)
        Assert.assertFalse(ERROR_B in error)

        error += ERROR_B
        Assert.assertTrue(ERROR_A in error)
        Assert.assertTrue(ERROR_B in error)
    }

    @Test
    fun remove() {
        val error = ComposableError()

        error += ERROR_A
        error += ERROR_B
        Assert.assertTrue(ERROR_A in error)
        Assert.assertTrue(ERROR_B in error)

        error -= ERROR_A
        Assert.assertFalse(ERROR_A in error)
        Assert.assertTrue(ERROR_B in error)
    }

    @Test
    fun clear() {
        val error = ComposableError()

        Assert.assertFalse(error.hasAny())
        error += ERROR_A
        error += ERROR_B
        Assert.assertTrue(ERROR_A in error)
        Assert.assertTrue(ERROR_B in error)
        Assert.assertTrue(error.hasAny())

        error -= ERROR_A
        error -= ERROR_B
        Assert.assertFalse(error.hasAny())
    }

    companion object {
        private const val ERROR_A = 1
        private const val ERROR_B = 1 shl 1
    }
}
