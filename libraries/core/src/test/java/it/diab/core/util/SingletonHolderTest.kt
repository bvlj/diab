/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SingletonHolderTest {

    @Test
    fun singletonHolder() {
        val a = TestData.getInstance("abc")
        val b = TestData.getInstance("def")

        assertEquals(a, b)
    }

    private class TestData private constructor(
        private val uid: String
    ) {

        override fun toString() = uid

        override fun equals(other: Any?) = other != null &&
            other is TestData &&
            other.toString() == toString()

        override fun hashCode() = toString().hashCode()

        companion object : SingletonHolder<TestData, String> ({ TestData(it) })
    }
}
