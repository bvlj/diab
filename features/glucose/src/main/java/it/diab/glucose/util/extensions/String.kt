/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.util.extensions

import java.util.Locale

internal fun String.upperCaseFirstChar() =
    substring(0, 1).toUpperCase(Locale.ROOT) + substring(1, length)
