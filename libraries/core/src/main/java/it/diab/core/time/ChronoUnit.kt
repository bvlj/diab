/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
@file:JvmName("ChronoUtils")
@file:Suppress("unused")

package it.diab.core.time

import java.util.concurrent.TimeUnit

data class ChronoUnit(internal val value: Long, internal val unit: TimeUnit) {

    fun toMillis() = unit.toMillis(value)
}

fun Long.millis() = ChronoUnit(this, TimeUnit.MILLISECONDS)

fun Long.seconds() = ChronoUnit(this, TimeUnit.SECONDS)

fun Long.minutes() = ChronoUnit(this, TimeUnit.MINUTES)

fun Long.hours() = ChronoUnit(this, TimeUnit.HOURS)

fun Long.days() = ChronoUnit(this, TimeUnit.DAYS)
