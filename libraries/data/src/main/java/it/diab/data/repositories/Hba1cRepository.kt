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
import it.diab.data.dao.Hba1cDao
import it.diab.data.entities.Hba1c
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class Hba1cRepository private constructor(private val hba1cDao: Hba1cDao) : BaseRepository() {

    val all = hba1cDao.all

    suspend fun getAllItems() = withContext(IO) {
        hba1cDao.getAllItems()
    }

    suspend fun insert(item: Hba1c) = withContext(IO) {
        hba1cDao.insert(item)
    }

    suspend fun delete(item: Hba1c) = withContext(IO) {
        hba1cDao.delete(item)
    }

    companion object : SingletonHolder<Hba1cRepository, Context>({
        Hba1cRepository(AppDatabase.getInstance(it).hba1c())
    })
}
