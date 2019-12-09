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

package xyz.diab.core.model

import xyz.diab.core.extension.fmt

/**
 * An [Insulin] bolus representation.
 * To be used in [Glucose] instances.
 *
 * @see Glucose
 * @see Bolus
 */
data class Bolus(
    val name: String = "",
    val value: Float = 0f
) {

    /**
     * String format to be used in the UI
     */
    fun fmt(): String = "$name ${value.fmt(1)}"
}
