/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.util.extensions

import it.diab.core.time.DateTime
import it.diab.core.time.DateTimeFormatter

internal fun DateTime.getDetailedString() =
    DateTimeFormatter("EEE dd-MM HH:mm").format(this).upperCaseFirstChar()
