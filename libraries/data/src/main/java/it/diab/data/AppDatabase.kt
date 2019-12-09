/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import it.diab.core.time.DateTime
import it.diab.core.util.SingletonHolder
import it.diab.data.dao.GlucoseDao
import it.diab.data.dao.Hba1cDao
import it.diab.data.dao.InsulinDao
import it.diab.data.entities.Glucose
import it.diab.data.entities.Hba1c
import it.diab.data.entities.Insulin
import it.diab.data.extensions.asTimeFrame

@Database(
    entities = [
        Glucose::class,
        Hba1c::class,
        Insulin::class
    ], version = 6
)
internal abstract class AppDatabase protected constructor() : RoomDatabase() {

    abstract fun glucose(): GlucoseDao
    abstract fun insulin(): InsulinDao
    abstract fun hba1c(): Hba1cDao

    companion object : SingletonHolder<AppDatabase, Context>({
        if (AppDatabase.TEST_MODE)
            Room.inMemoryDatabaseBuilder(it, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        else
            Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "diab_database")
                .addMigrations(
                    AppDatabase.MIGRATION_1_2,
                    AppDatabase.MIGRATION_2_3,
                    AppDatabase.MIGRATION_3_4,
                    AppDatabase.MIGRATION_4_5,
                    AppDatabase.MIGRATION_5_6
                )
                .build()
    }) {
        // This is used during unit tests
        var TEST_MODE = false

        /*
         * DB version 2
         *
         * Glucose
         *     Remove "heavyMeal" column [Boolean]
         *     Add "eatLevel" column [Int]
         *
         * Insulin
         *     Add "isBasal" column [Boolean]
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /* Glucose */
                // Edit the schema by creating a new "cloned" table
                database.execSQL("ALTER TABLE glucose RENAME TO tmp_glucose")
                database.execSQL("DROP INDEX IF EXISTS index_glucose_date_uid")

                database.execSQL("CREATE TABLE glucose (uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, value INTEGER NOT NULL, date INTEGER NOT NULL, insulinId0 INTEGER NOT NULL, insulinValue0 REAL NOT NULL, insulinId1 INTEGER NOT NULL, insulinValue1 REAL NOT NULL, eatLevel INTEGER NOT NULL DEFAULT 1)")
                // Copy the content
                database.execSQL("INSERT INTO glucose(uid, value, date, insulinId0, insulinValue0, insulinId1, insulinValue1) SELECT uid, value, date, insulinId0, insulinValue0, insulinId1, insulinValue1 FROM tmp_glucose")
                database.execSQL("CREATE UNIQUE INDEX index_glucose_date_uid ON glucose (date, uid)")
                // Drop old table
                database.execSQL("DROP TABLE tmp_glucose")

                /* Insulin */
                database.execSQL("ALTER TABLE insulin ADD COLUMN isBasal INTEGER NOT NULL DEFAULT 0")
            }
        }

        /*
         * DB version 3
         *
         * Insulin
         *     Add "hasHalfUnits" column [Boolean]
         */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /* Insulin */
                database.execSQL("ALTER TABLE insulin ADD COLUMN hasHalfUnits INTEGER NOT NULL DEFAULT 0")
            }
        }

        /*
         * DB version 4
         *
         * Glucose
         *     Add "timeFrame" column [Int]
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /* Glucose */
                // Add the column
                database.execSQL("ALTER TABLE glucose ADD COLUMN timeFrame INTEGER NOT NULL DEFAULT -1")

                // Apply the column changes
                database.setTransactionSuccessful()
                database.endTransaction()

                // Reopen the db again to set the value for all the existing items
                database.beginTransaction()
                val cursor = database.query("SELECT uid, date FROM glucose ORDER BY uid DESC")

                if (!cursor.moveToFirst()) {
                    return
                }

                do {
                    val item = ContentValues()
                    val uid = cursor.getLong(0)
                    val date = DateTime(cursor.getLong(1))
                    item.put("timeFrame", date.asTimeFrame().ordinal - 1)

                    database.update(
                        "glucose", SQLiteDatabase.CONFLICT_REPLACE, item,
                        "uid = ?", arrayOf("$uid")
                    )
                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        /*
         * DB version 5
         *
         * Hba1c
         *     Add new table
         */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /* Hba1c */
                database.execSQL("CREATE TABLE IF NOT EXISTS `hba1c` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `value` REAL NOT NULL, `date` INTEGER NOT NULL)")
            }
        }

        /*
         * DB version 6
         *
         * Glucose
         *     Increase all "timeFrame" values by 1
         * Insulin
         *     Increase all "timeFrame" values by 1
         */
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("UPDATE glucose SET timeFrame = timeFrame + 1")
                database.execSQL("UPDATE insulin SET timeFrame = timeFrame + 1")
            }
        }
    }
}
