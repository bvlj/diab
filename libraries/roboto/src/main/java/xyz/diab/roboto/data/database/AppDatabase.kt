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

package xyz.diab.roboto.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.diab.core.util.SingletonHolder
import xyz.diab.roboto.data.database.dao.GlucoseDao
import xyz.diab.roboto.data.database.dao.InsulinDao
import xyz.diab.roboto.data.database.entity.GlucoseEntity
import xyz.diab.roboto.data.database.entity.Hba1cEntity
import xyz.diab.roboto.data.database.entity.InsulinEntity

@Database(
    entities = [
        GlucoseEntity::class,
        Hba1cEntity::class,
        InsulinEntity::class
    ],
    version = 1
)
internal abstract class AppDatabase protected constructor() : RoomDatabase() {

    abstract fun glucoseDao(): GlucoseDao
    abstract fun insulinDao(): InsulinDao

    internal companion object : SingletonHolder<AppDatabase, Config>({
        when (it) {
            is Config.Persistent ->
                Room.databaseBuilder(it.context, AppDatabase::class.java, AppDatabase.DB_NAME)
                    .build()
            is Config.InMemory ->
                Room.inMemoryDatabaseBuilder(it.context, AppDatabase::class.java)
                    .build()
        }
    }) {
        private const val DB_NAME = "appDatabase.db"
    }

    internal sealed class Config {

        class Persistent(val context: Context) : Config()
        class InMemory(val context: Context) : Config()
    }
}
