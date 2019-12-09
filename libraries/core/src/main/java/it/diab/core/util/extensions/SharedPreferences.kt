/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.util.extensions

import android.content.SharedPreferences

inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T {
    return when (T::class) {
        Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
        Float::class -> getFloat(key, defaultValue as? Float ?: 0f) as T
        Int::class -> getInt(key, defaultValue as? Int ?: 0) as T
        Long::class -> getLong(key, defaultValue as? Long ?: 0) as T
        String::class -> getString(key, defaultValue as? String ?: "") as T
        else -> throw IllegalArgumentException("Type ${T::class.java.simpleName} is not supported")
    }
}

inline operator fun <reified T : Any> SharedPreferences.set(key: String, value: T) {
    val editor = edit()
    editor.run {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            else -> throw IllegalArgumentException("Type ${T::class.java.simpleName} is not supported")
        }
    }
    editor.apply()
}
