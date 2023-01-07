package com.rmarinov.tempcheck

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rmarinov.tempcheck.data.model.Direction

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class AlertsFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val target = remoteMessage.data["target"]?.toFloatOrNull()
            val direction = remoteMessage.data["direction"]

            if (target != null && direction != null) {
                sendNotification(target, direction)
            }
        }
    }

    private fun sendNotification(target: Float, direction: String) {
        val channelId = getString(R.string.notification_channel_alerts)
        val directionString = if (direction == Direction.OVER.toString()) {
            getString(R.string.over)
        } else {
            getString(R.string.under)
        }
        val formattedTarget = getString(R.string.temperature_format, target)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_alert)
            .setContentTitle("The temperature is $directionString $formattedTarget.")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            getString(R.string.alerts),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }
}
