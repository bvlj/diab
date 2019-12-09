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

package xyz.diab.roboto.data.datasource

import android.content.Context
import xyz.diab.core.data.glucose.GlucoseDataSource
import xyz.diab.core.data.insulin.InsulinDataSource
import xyz.diab.roboto.App
import xyz.diab.roboto.data.database.AppDatabase

class DataSourceFactory(context: Context) {
    @Suppress("ConstantConditionIf")
    private val db = AppDatabase.getInstance(
        if (App.TEST_MODE) AppDatabase.Config.Persistent(context)
        else AppDatabase.Config.Persistent(context)
    )

    fun provideGlucoseDataSource(): GlucoseDataSource =
        GlucoseDataSourceImpl(db.glucoseDao())

    fun provideInsulinDataSource(): InsulinDataSource =
        InsulinDataSourceImpl(db.insulinDao())
}
