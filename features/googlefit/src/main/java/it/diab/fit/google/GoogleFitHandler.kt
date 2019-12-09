/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.fit.google

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.HealthDataTypes
import com.google.android.gms.fitness.data.HealthFields
import com.google.android.gms.fitness.request.DataUpdateRequest
import it.diab.core.override.BaseFitHandler
import it.diab.data.entities.Glucose
import it.diab.fit.google.ui.GoogleFitFragment
import it.diab.fit.google.util.extensions.toFitMealRelation
import it.diab.fit.google.util.extensions.toFitSleepRelation
import java.util.concurrent.TimeUnit

@Suppress("unused")
class GoogleFitHandler : BaseFitHandler() {

    override val isEnabled = true

    override fun hasFit(context: Context): Boolean {
        val availability = GoogleApiAvailability.getInstance()
        val status = availability.isGooglePlayServicesAvailable(context)

        if (status != ConnectionResult.SUCCESS) {
            return false
        }

        val options = FitnessOptions.builder()
            .addDataType(HealthDataTypes.TYPE_BLOOD_GLUCOSE, FitnessOptions.ACCESS_WRITE)
            .build()

        return GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(context), options)
    }

    override fun getFragment() = GoogleFitFragment()

    override fun upload(
        context: Context,
        item: Any,
        isNew: Boolean,
        onCompletion: (Boolean) -> Unit
    ) {
        if (item !is Glucose) {
            Log.e(TAG, "Parameter item is not of type Glucose")
            onCompletion(false)
            return
        }

        val set = prepareData(item)
        val timeStamp = item.date.epochMillis
        val request = DataUpdateRequest.Builder()
            .setDataSet(set)
            .setTimeInterval(timeStamp, timeStamp, TimeUnit.MILLISECONDS)
            .build() ?: return

        val account = GoogleSignIn.getLastSignedInAccount(context) ?: return
        Fitness.getHistoryClient(context, account)
            .run { if (isNew) insertData(set) else updateData(request) }
            .addOnFailureListener { onCompletion(false) }
            .addOnSuccessListener { onCompletion(true) }
    }

    private fun prepareData(glucose: Glucose): DataSet {
        val timeStamp = glucose.date.epochMillis
        val source = DataSource.Builder()
            .setType(DataSource.TYPE_RAW)
            .setDataType(HealthDataTypes.TYPE_BLOOD_GLUCOSE)
            .build()

        val point = DataPoint.builder(source)
            .setTimestamp(timeStamp, TimeUnit.MILLISECONDS)
            .setField(HealthFields.FIELD_BLOOD_GLUCOSE_LEVEL, glucose.value / 18f)
            .setField(
                HealthFields.FIELD_BLOOD_GLUCOSE_SPECIMEN_SOURCE,
                HealthFields.BLOOD_GLUCOSE_SPECIMEN_SOURCE_CAPILLARY_BLOOD
            )
            .setField(
                HealthFields.FIELD_TEMPORAL_RELATION_TO_MEAL,
                glucose.timeFrame.toFitMealRelation()
            )
            .setField(
                HealthFields.FIELD_TEMPORAL_RELATION_TO_SLEEP,
                glucose.timeFrame.toFitSleepRelation()
            )
            .build()

        return DataSet.builder(source).add(point)
            .build()
    }

    companion object {
        private const val TAG = "GoogleFitHandler"
    }
}
