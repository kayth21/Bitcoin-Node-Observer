package com.ceaver.bno.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.ceaver.bno.Application
import com.ceaver.bno.MainActivity
import com.ceaver.bno.R
import java.util.*


object Notification {

    const val CHANNEL_ID = "com.ceaver.bno.notification.Notification.ChannelId"

    init {
        val name = "Bitcoin Node Observer notification channel"
        val description = "Notification on node status change"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply { this.description = description }
        val notificationManager = getSystemService(Application.appContext!!, NotificationManager::class.java)!!
        notificationManager.createNotificationChannel(channel)
    }

    fun notifyStatusChange(title: String, text: String, image: Int) {

        val intent = Intent(Application.appContext!!, MainActivity::class.java).apply { this.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) }
        val pendingIntent = PendingIntent.getActivity(Application.appContext!!, 0, intent, 0)

        val notification = NotificationCompat.Builder(Application.appContext!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.bno_notification)
            .setLargeIcon(BitmapFactory.decodeResource(Application.appContext!!.resources, image))
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(Application.appContext!!).notify(Random().nextInt(), notification);
    }
}