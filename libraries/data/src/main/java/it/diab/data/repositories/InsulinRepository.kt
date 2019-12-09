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
import it.diab.core.util.SingletonHolder
import it.diab.data.AppDatabase
import it.diab.data.dao.InsulinDao
import it.diab.data.entities.Insulin
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class InsulinRepository private constructor(private val dao: InsulinDao) : BaseRepository() {

    val all = dao.all

    suspend fun getInsulins() = withContext(IO) {
        dao.getInsulins()
    }

    suspend fun getBasals() = withContext(IO) {
        dao.getBasals()
    }

    suspend fun getById(uid: Long) = withContext(IO) {
        dao.getById(uid).firstOrNull() ?: Insulin()
    }

    suspend fun insert(insulin: Insulin) = withContext(IO) {
        dao.insert(insulin)
    }

    suspend fun delete(insulin: Insulin) = withContext(IO) {
        dao.delete(insulin)
    }

    companion object : SingletonHolder<InsulinRepository, Context>({
        InsulinRepository(AppDatabase.getInstance(it).insulin())
    })
}
