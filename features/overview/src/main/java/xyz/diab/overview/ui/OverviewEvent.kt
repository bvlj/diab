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

package xyz.diab.overview.ui

import xyz.diab.core.model.Glucose
import xyz.diab.roboto.events.Event

internal sealed class OverviewEvent : Event {

    data class DataChanged(val dataList: List<Glucose>) : OverviewEvent()

    data class ItemClicked(val id: Long) : OverviewEvent()

    data class DeleteItem(val id: Long) : OverviewEvent()

    data class ItemDeleted(val item: Glucose) : OverviewEvent()

    data class DeletionUndone(val item: Glucose) : OverviewEvent()
}
