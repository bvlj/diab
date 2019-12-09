/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.util.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SharedPreferencesTest {

    private lateinit var prefs: SharedPreferences

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        prefs = context.getSharedPreferences("_test", Context.MODE_PRIVATE)
    }

    @Test
    fun boolTest() {
        Assert.assertFalse(prefs[KEY_BOOL])
        Assert.assertFalse(prefs[KEY_BOOL, false])
        Assert.assertTrue(prefs[KEY_BOOL, true])

        prefs[KEY_BOOL] = true
        Assert.assertTrue(prefs[KEY_BOOL])
        Assert.assertTrue(prefs[KEY_BOOL, true])
        Assert.assertTrue(prefs[KEY_BOOL, false])
    }

    @Test
    fun floatTest() {
        Assert.assertEquals(0f, prefs[KEY_FLOAT], 0f)
        Assert.assertNotEquals(1f, prefs[KEY_FLOAT], 0f)
        Assert.assertEquals(1f, prefs[KEY_FLOAT, 1f], 0f)

        prefs[KEY_FLOAT] = 1f
        Assert.assertEquals(1f, prefs[KEY_FLOAT], 0f)
        Assert.assertEquals(1f, prefs[KEY_FLOAT, 0f], 0f)
        Assert.assertEquals(1f, prefs[KEY_FLOAT, 1f], 0f)
    }

    @Test
    fun intTest() {
        Assert.assertEquals(1, prefs[KEY_INT, 1])

        prefs[KEY_INT] = 1
        Assert.assertEquals(1, prefs[KEY_INT, 0])
        Assert.assertEquals(1, prefs[KEY_INT, 1])
    }

    @Test
    fun longTest() {
        Assert.assertEquals(1L, prefs[KEY_LONG, 1L])

        prefs[KEY_LONG] = 1L
        Assert.assertEquals(1L, prefs[KEY_LONG, 0L])
        Assert.assertEquals(1L, prefs[KEY_LONG, 1L])
    }

    @Test
    fun strTest() {
        Assert.assertEquals("a", prefs[KEY_STR, "a"])

        prefs[KEY_STR] = "a"
        Assert.assertEquals("a", prefs[KEY_STR, ""])
        Assert.assertEquals("a", prefs[KEY_STR, "a"])
    }

    @Test(expected = IllegalArgumentException::class)
    fun unsupportedGet() {
        prefs[KEY_UNS, 'c']

        Assert.fail()
    }

    @Test(expected = IllegalArgumentException::class)
    fun unsupportedSet() {
        prefs[KEY_UNS] = 'c'

        Assert.fail()
    }

    @After
    fun tearDown() {
        prefs.edit()
            .clear()
            .commit()
    }

    companion object {
        private const val KEY_BOOL = "b"
        private const val KEY_FLOAT = "f"
        private const val KEY_INT = "i"
        private const val KEY_LONG = "l"
        private const val KEY_STR = "s"
        private const val KEY_UNS = "s"
    }
}
