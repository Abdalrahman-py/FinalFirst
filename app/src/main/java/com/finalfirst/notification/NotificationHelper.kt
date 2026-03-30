package com.finalfirst.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.finalfirst.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "contacts_channel"
        private const val CHANNEL_NAME = "Contacts"
        private const val NOTIFICATION_ID = 1001
    }

    fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        manager.createNotificationChannel(channel)
    }

    fun sendNewContactNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("جهات الاتصال")
            .setContentText("تم اضافة رقم جديد")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            manager.notify(NOTIFICATION_ID, notification)
        }
    }
}
