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

package xyz.diab.roboto.data

import android.content.Context
import xyz.diab.core.data.glucose.GlucoseRepository
import xyz.diab.core.data.insulin.InsulinRepository
import xyz.diab.roboto.data.datasource.DataSourceFactory

object RobotoRepositoryFactory {
    @Volatile
    private var dataSourceFactory: DataSourceFactory? = null
    @Volatile
    private var glucoseRepo: GlucoseRepository? = null
    @Volatile
    private var insulinRepo: InsulinRepository? = null

    private fun getDataSourceFactory(context: Context) =
        dataSourceFactory ?: synchronized(this) {
            DataSourceFactory(context).also { dataSourceFactory = it }
        }

    fun provideGlucoseRepo(context: Context) =
        glucoseRepo ?: synchronized(this) {
            val dataSource = getDataSourceFactory(context).provideGlucoseDataSource()
            GlucoseRepository(dataSource).also { glucoseRepo = it }
        }

    fun provideInsulinRepo(context: Context) =
        insulinRepo ?: synchronized(this) {
            val dataSource = getDataSourceFactory(context).provideInsulinDataSource()
            InsulinRepository(dataSource).also { insulinRepo = it }
        }
}
