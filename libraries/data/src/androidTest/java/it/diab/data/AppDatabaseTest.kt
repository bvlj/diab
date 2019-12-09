/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import it.diab.data.dao.GlucoseDao
import it.diab.data.dao.InsulinDao
import it.diab.data.entities.Glucose
import it.diab.data.entities.TimeFrame
import it.diab.data.extensions.glucose
import it.diab.data.extensions.insulin
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AppDatabaseTest {
    private lateinit var glucoseDao: GlucoseDao
    private lateinit var insulinDao: InsulinDao

    @Before
    fun setup() {
        AppDatabase.TEST_MODE = true
        val context = ApplicationProvider.getApplicationContext<Context>()
        val instance = AppDatabase.getInstance(context)
        glucoseDao = instance.glucose()
        insulinDao = instance.insulin()
    }

    @Test
    fun addGlucose() = runBlocking {
        val item = glucose {
            uid = 1
            value = 100
            eatLevel = Glucose.MEDIUM
            timeFrame = TimeFrame.MORNING
        }

        glucoseDao.insert(item)
        val test = glucoseDao.getById(1)[0]
        Assert.assertEquals(test, item)
    }

    @Test
    fun addInsulin() = runBlocking {
        val item = insulin {
            uid = 1
            name = "TEST 0"
            timeFrame = TimeFrame.LUNCH
            hasHalfUnits = true
        }.also { insulinDao.insert(it) }

        val test = insulinDao.getById(1)[0]
        Assert.assertEquals(test, item)
    }

    @Test
    fun addGlucoseWithLinkedInsulins() = runBlocking {
        val insulin = insulin {
            uid = 2
            name = "TEST 1"
            timeFrame = TimeFrame.DINNER
        }.also { insulinDao.insert(it) }

        val basal = insulin {
            uid = 3
            name = "TEST 2"
            timeFrame = TimeFrame.DINNER
            isBasal = true
            hasHalfUnits = true
        }.also { insulinDao.insert(it) }

        glucoseDao.insert(glucose {
            uid = 2
            value = 100
            insulinId0 = insulin.uid
            insulinValue0 = 3f
            insulinId1 = basal.uid
            insulinValue1 = 4f
            eatLevel = Glucose.HIGH
            timeFrame = TimeFrame.EXTRA
        })

        glucoseDao.getById(2)[0].run {
            val test1 = insulinDao.getById(insulinId0)[0]
            val test2 = insulinDao.getById(insulinId1)[0]

            Assert.assertEquals(test1.uid, insulin.uid)
            Assert.assertEquals(test2.uid, basal.uid)
        }
    }
}
