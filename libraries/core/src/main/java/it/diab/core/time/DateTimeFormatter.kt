/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.core.time

import java.text.SimpleDateFormat
import java.util.Locale

class DateTimeFormatter(pattern: String) {
    private val _formatter = SimpleDateFormat(pattern, Locale.getDefault())

    fun format(dateTime: DateTime): String =
        _formatter.format(dateTime.asJavaDate())
}
