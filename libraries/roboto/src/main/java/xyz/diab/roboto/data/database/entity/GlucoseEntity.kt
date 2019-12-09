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

package xyz.diab.roboto.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import xyz.diab.core.model.Bolus

@Entity(
    tableName = "glucoses",
    foreignKeys = [
        ForeignKey(
            entity = InsulinEntity::class,
            childColumns = ["bolus_name"],
            parentColumns = ["name"],
            onDelete = ForeignKey.SET_DEFAULT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = InsulinEntity::class,
            childColumns = ["basal_name"],
            parentColumns = ["name"],
            onDelete = ForeignKey.SET_DEFAULT,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("bolus_name", name = "bolusIndex"),
        Index("basal_name", name = "basalIndex")
    ]
)
internal data class GlucoseEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Long,
    val value: Float,
    val timeStamp: Long,
    val beforeMeal: Boolean,
    val eatLevel: Int,
    @Embedded(prefix = "bolus_")
    val bolus: Bolus? = null,
    @Embedded(prefix = "basal_")
    val basalBolus: Bolus? = null
)
