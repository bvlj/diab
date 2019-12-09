/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.export.writer

import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import it.diab.data.entities.Glucose
import it.diab.data.repositories.GlucoseRepository
import it.diab.data.repositories.InsulinRepository
import it.diab.export.BuildConfig
import it.diab.export.utils.extensions.setAlternateBackground
import it.diab.export.utils.extensions.write
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.dhatim.fastexcel.Workbook
import org.dhatim.fastexcel.Worksheet

internal class XlsxWriter(
    private val scope: CoroutineScope,
    private val outDescriptor: ParcelFileDescriptor,
    private val glucoseRepository: GlucoseRepository,
    private val insulinRepository: InsulinRepository
) {
    suspend fun exportSheet(
        glucoseHeaders: List<String>,
        insulinHeaders: List<String>
    ): Boolean {

        outDescriptor.use { parcelDescriptor ->
            // We need to make sure the parcel descriptor is also closed when we're done
            FileOutputStream(parcelDescriptor.fileDescriptor).use {
                val workBook = Workbook(it, BuildConfig.LIBRARY_PACKAGE_NAME, null)
                val glucoseSheet = workBook.newWorksheet("Glucose")
                val insulinSheet = workBook.newWorksheet("Insulin")

                val glucoseDeferred = scope.async {
                    buildGlucoseSheet(glucoseSheet, glucoseRepository, insulinRepository, glucoseHeaders)
                }

                val insulinDeferred = scope.async {
                    buildInsulinSheet(insulinSheet, insulinRepository, insulinHeaders)
                }

                return try {
                    glucoseDeferred.await()
                    insulinDeferred.await()
                    workBook.finish()
                    true
                } catch (e: IOException) {
                    Log.e(TAG, e.message, e)
                    false
                }
            }
        }
    }

    private suspend fun buildGlucoseSheet(
        sheet: Worksheet,
        glucoseRepository: GlucoseRepository,
        insulinRepository: InsulinRepository,
        headers: List<String>
    ) {
        headers.forEachIndexed { i, str -> sheet.value(0, i, str) }
        sheet.style(0, headers.size - 1).bold()

        val list = glucoseRepository.getAllItems()

        // Kotlin goes mad if we write a suspend function inside a forEachIndexed code block
        var i = 0
        while (i < list.size) {
            val glucose = list[i]
            writeGlucoseRow(sheet, insulinRepository, glucose, i)
            i++
        }

        sheet.setAlternateBackground(list.size, headers.size - 1)

        // Java 8's java.time.LocalDateTime is required for this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sheet.range(1, 1, list.size, 1)
                .style()
                .format(DATE_FORMAT)
                .set()
        }
    }

    private suspend fun writeGlucoseRow(
        sheet: Worksheet,
        insulinRepository: InsulinRepository,
        glucose: Glucose,
        position: Int
    ) {
        val insulin = insulinRepository.getById(glucose.insulinId0)
        val basal = insulinRepository.getById(glucose.insulinId1)

        sheet.apply {
            val row = position + 1

            write(
                glucose.value to (row to 0),
                glucose.date.format(DATE_FORMAT) to (row to 1),
                glucose.eatLevel to (row to 2)
            )

            if (insulin.name.isNotEmpty()) {
                write(
                    insulin.name to (row to 3),
                    glucose.insulinValue0 to (row to 4)
                )
            }

            if (basal.name.isNotEmpty()) {
                write(
                    basal.name to (row to 5),
                    glucose.insulinValue1 to (row to 6)
                )
            }
        }
    }

    private suspend fun buildInsulinSheet(
        sheet: Worksheet,
        insulinRepository: InsulinRepository,
        headers: List<String>
    ) {
        headers.forEachIndexed { i, str -> sheet.value(0, i, str) }
        sheet.style(0, headers.size - 1).bold()

        val list = insulinRepository.getInsulins()
        list.forEachIndexed { i, insulin ->
            val row = i + 1
            sheet.write(
                insulin.name to (row to 0),
                insulin.timeFrame.name to (row to 1),
                (if (insulin.isBasal) "TRUE" else "FALSE") to (row to 2),
                (if (insulin.hasHalfUnits) "TRUE" else "FALSE") to (row to 3)
            )
        }

        sheet.setAlternateBackground(list.size, headers.size - 1)
    }

    companion object {
        private const val TAG = "ExportService"
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm"
    }
}
