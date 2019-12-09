/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.glucose.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import it.diab.core.util.Activities
import it.diab.core.util.intentTo
import it.diab.glucose.R

internal class CheckAgainWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildChannel(notificationManager)
        }

        val pIntent = PendingIntent.getActivity(
            context, 0,
            intentTo(Activities.Glucose.Editor), PendingIntent.FLAG_CANCEL_CURRENT
        )

        val message = context.getString(R.string.check_again_notification_content)
        val notification = NotificationCompat.Builder(context, CHANNEL)
            .setContentTitle(context.getString(R.string.check_again_notification_title))
            .setContentText(message.substring(0, 100))
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentIntent(pIntent)
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setSmallIcon(R.drawable.ic_idea)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)

        return Result.success()
    }

    @RequiresApi(26)
    private fun buildChannel(notificationManager: NotificationManager) {
        val existing = notificationManager.getNotificationChannel(CHANNEL)
        if (existing != null) {
            return
        }

        val newChannel = NotificationChannel(
            CHANNEL,
            context.getString(R.string.check_again_notification_channel),
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(newChannel)
    }

    companion object {
        private const val CHANNEL = "checkAgainChannel"
        private const val NOTIFICATION_ID = 1927
    }
}
