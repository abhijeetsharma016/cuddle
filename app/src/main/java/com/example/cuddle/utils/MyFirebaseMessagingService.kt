package com.example.cuddle.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cuddle.MainActivity
import com.example.cuddle.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val channelId = "cuddle"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
            createNotificationChannel(manager as NotificationManager)

        val intent1 = PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE)

        val notification = android.app.Notification.Builder(this, channelId)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.notification_icon)
            .setAutoCancel(true)
            .setContentIntent(intent1)
            .build()

        manager.notify(Random.nextInt(), notification)

    }

    private fun createNotificationChannel(manager: NotificationManager){
        val channel = NotificationChannel(channelId, "Cuddlechat", NotificationManager.IMPORTANCE_HIGH)


        channel.description = "New Chat"
        channel.enableLights(true)
        manager.createNotificationChannel(channel)
    }
}