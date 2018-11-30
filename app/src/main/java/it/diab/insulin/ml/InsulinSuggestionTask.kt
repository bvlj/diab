/*
 * Copyright (c) 2018 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.ml

import android.os.AsyncTask
import it.diab.db.entities.Glucose
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

class InsulinSuggestionTask(private val mBridge: PluginBridge,
                            private val mOnExecuted: (Float) -> Unit) :
        AsyncTask<Glucose, Unit, Float>() {

    override fun doInBackground(vararg params: Glucose?): Float {

        val glucose = params[0] ?: throw IllegalArgumentException("You must provide a glucose")
        val estimatorStream = mBridge.getStreamFor(glucose.timeFrame)

        val eatLevelFix = glucose.eatLevel - 1

        val estimatorMap = parseEstimator(estimatorStream)

        if (estimatorMap.isEmpty()) {
            return NO_MODEL
        }

        // Round down the last digit
        val targetValue = (glucose.value / 10) * 10

        if (targetValue > 420) {
            return TOO_HIGH
        } else if (targetValue < 50) {
            return TOO_LOW
        }

        var result = PARSE_ERROR

        for (key in estimatorMap.keys) {
            if (key == targetValue) {
                result = estimatorMap[key] ?: PARSE_ERROR
                break
            }
        }

        // Take a small nap to allow UI to show animations
        Thread.sleep(500)

        return if (result == PARSE_ERROR) PARSE_ERROR else result + eatLevelFix
    }

    override fun onPostExecute(result: Float?) {
        mOnExecuted(result ?: PARSE_ERROR)
    }

    private fun parseEstimator(input: InputStream): HashMap<Int, Float> {
        val map = HashMap<Int, Float>()
        val content = BufferedReader(InputStreamReader(input)).lines()
                .parallel()
                .collect(Collectors.joining("\n"))

        val json = JSONObject(content)
        val iterator = json.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            val value = json[key] as Double

            map[key.toInt()] = value.toFloat()
        }

        input.close()
        return map
    }

    companion object {
        const val TOO_LOW = -1f
        const val TOO_HIGH = -2f
        const val PARSE_ERROR = -3f
        const val NO_MODEL = -4f
    }
}