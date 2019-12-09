/*
 * Copyright (c) 2020 Bevilacqua Joey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.diab.roboto.util

import java.util.Calendar

object TimeUtil {
    private const val SECOND = 1000L
    private const val MINUTE = SECOND * 60L
    private const val HOUR = MINUTE * 60L
    private const val DAY = HOUR * 24L

    fun getNowTimeStamp() =
        System.currentTimeMillis()

    fun getDaysAgoTimeStamp(days: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis() - (DAY * days)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
