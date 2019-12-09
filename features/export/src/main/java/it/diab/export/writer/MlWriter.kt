/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.export.writer

import android.os.ParcelFileDescriptor
import it.diab.core.time.DateTime
import it.diab.core.time.days
import it.diab.data.entities.Glucose
import it.diab.data.entities.TimeFrame
import it.diab.data.repositories.GlucoseRepository
import it.diab.export.utils.extensions.splitBy
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal class MlWriter(
    private val parcelDescriptor: ParcelFileDescriptor,
    private val repository: GlucoseRepository,
    private val filterRange: IntRange
) {

    suspend fun export(): Boolean {
        val dateEnd = DateTime.now
        val dateStart = dateEnd - DAYS_TO_EXPORT.days()
        val end = dateEnd.epochMillis
        val start = dateStart.epochMillis

        ZipOutputStream(FileOutputStream(parcelDescriptor.fileDescriptor)).use { oStream ->
            return try {
                exportTrain(oStream, TimeFrame.MORNING, start, end)
                exportTrain(oStream, TimeFrame.LUNCH, start, end)
                exportTrain(oStream, TimeFrame.DINNER, start, end)

                exportTest(oStream, TimeFrame.MORNING, TimeFrame.LUNCH, start, end)
                exportTest(oStream, TimeFrame.LUNCH, TimeFrame.DINNER, start, end)
                exportTest(oStream, TimeFrame.DINNER, TimeFrame.NIGHT, start, end)
                true
            } catch (e: IOException) {
                false
            }
        }
    }

    private suspend fun exportTrain(
        zipStream: ZipOutputStream,
        timeFrame: TimeFrame,
        start: Long,
        end: Long
    ) {
        zipStream.putNextEntry(ZipEntry(BASE_TRAIN.format(timeFrame.ordinal)))

        val list = repository.getInDateRange(start, end)
            .filter { it.insulinId0 > 0 && it.value in filterRange && it.timeFrame == timeFrame }

        val content = StringBuilder().run {
            append(COLUMNS_HEADER)
            list.forEach {
                append("${it.value},${it.eatLevel},${it.insulinValue0}\n")
            }

            toString()
        }
        content.toByteArray().splitBy(2048).forEach(zipStream::write)
        // DON'T CLOSE THE STREAM HERE
    }

    private suspend fun exportTest(
        zipStream: ZipOutputStream,
        targetTimeFrame: TimeFrame,
        nextTimeFrame: TimeFrame,
        start: Long,
        end: Long
    ) {
        zipStream.putNextEntry(ZipEntry(BASE_TEST.format(targetTimeFrame.ordinal)))

        val list = repository.getInDateRange(start, end)
            .filter { it.timeFrame == targetTimeFrame && it.insulinValue0 > 0 }

        val content = StringBuilder().run {
            append(COLUMNS_HEADER)
            getGoodOutcomes(list, targetTimeFrame, nextTimeFrame).forEach {
                append("${it.value},${it.eatLevel},${it.insulinValue0}\n")
            }

            toString()
        }

        content.toByteArray().splitBy(2048).forEach(zipStream::write)
        // DON'T CLOSE THE STREAM HERE
    }

    private fun getGoodOutcomes(
        list: List<Glucose>,
        targetTimeFrame: TimeFrame,
        nextTimeFrame: TimeFrame
    ): List<Glucose> {
        val result = mutableListOf<Glucose>()
        var i = list.size - 1
        while (i > 0) {
            val item = list[i]
            i--

            if (item.timeFrame != targetTimeFrame) {
                // Not a timeFrame we want
                continue
            }

            // We already moved the index to the next element
            val next = list[i]
            if (next.timeFrame.ordinal > nextTimeFrame.ordinal) {
                // This is too far from a desirable target to see whether the outcome was good
                continue
            }

            if (next.value in filterRange) {
                result.add(item)
            }
        }

        return result
    }

    companion object {
        private const val COLUMNS_HEADER = "value,eatLevel,insulin\n"
        private const val DAYS_TO_EXPORT = 60L

        private const val BASE_TRAIN = "train_%1\$s.csv"
        private const val BASE_TEST = "test_%1\$s.csv"
    }
}
