/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data.repositories

import android.content.Context
import it.diab.core.time.DateTime
import it.diab.core.util.SingletonHolder
import it.diab.data.AppDatabase
import it.diab.data.dao.GlucoseDao
import it.diab.data.entities.Glucose
import it.diab.data.entities.Insulin
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class GlucoseRepository private constructor(private val dao: GlucoseDao) : BaseRepository() {

    val all = dao.all

    val pagedList = dao.pagedList

    fun getByIdLive(uid: Long) = dao.getByIdLive(uid)

    suspend fun getById(uid: Long) = withContext(IO) {
        dao.getById(uid).firstOrNull() ?: Glucose()
    }

    suspend fun getAllItems() = withContext(IO) {
        dao.getAllItems()
    }

    suspend fun getAllDates() = withContext(IO) {
        dao.getAllDates()
    }

    suspend fun getInDateRange(start: Long, end: Long) = withContext(IO) {
        dao.getInDateRange(start, end)
    }

    suspend fun getInDateRange(start: DateTime, end: DateTime) =
        getInDateRange(start.epochMillis, end.epochMillis)

    suspend fun insert(glucose: Glucose) = withContext(IO) {
        dao.insert(glucose)
    }

    suspend fun delete(glucose: Glucose) = withContext(IO) {
        dao.delete(glucose)
    }

    suspend fun deleteInsulinValues(insulin: Insulin) = withContext(IO) {
        val insulinUid = insulin.uid
        val all = dao.getAllItems()
        all.filter { it.insulinId0 == insulinUid }
            .forEach {
                it.insulinId0 = -1
                it.insulinValue0 = 0f
            }

        all.filter { it.insulinId1 == insulinUid }
            .forEach {
                it.insulinId1 = -1
                it.insulinValue1 = 0f
            }
    }

    companion object : SingletonHolder<GlucoseRepository, Context>({
        GlucoseRepository(AppDatabase.getInstance(it).glucose())
    })
}
