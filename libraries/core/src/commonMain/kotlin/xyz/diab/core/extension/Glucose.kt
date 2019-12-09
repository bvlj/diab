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

package xyz.diab.core.extension

import xyz.diab.core.model.Glucose

val Glucose.isEmpty: Boolean
    get() = value == 0f

fun Glucose.isTheSame(other: Glucose): Boolean =
    this.id == other.id

fun Glucose.hasSameContent(other: Glucose): Boolean =
    this.beforeMeal == other.beforeMeal &&
        this.basalBolus == other.basalBolus &&
        this.bolus == other.bolus &&
        this.eatLevel == other.eatLevel &&
        this.timeStamp == other.timeStamp &&
        this.value == other.value
