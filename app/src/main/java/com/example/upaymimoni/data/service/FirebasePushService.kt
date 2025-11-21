package com.example.upaymimoni.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.upaymimoni.MainActivity
import com.example.upaymimoni.R
import com.example.upaymimoni.domain.repository.FcmTokenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FirebasePushService : FirebaseMessagingService() {

    // We have to use field injection, as constructor injection does not work.
    private val tokenRepo: FcmTokenRepository by inject()
    private val deviceIdService: DeviceIdService by inject()
    private val auth: FirebaseAuth by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val userId = auth.currentUser?.uid ?: return
        val deviceId = deviceIdService.deviceId

        CoroutineScope(Dispatchers.IO).launch {
            tokenRepo.saveToken(userId, deviceId, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.data["title"] ?: "UPayMiMoni"
        val body = message.data["body"] ?: "Someone requests your attention."

        showNotification(title, body)
    }

    private fun showNotification(title: String, body: String) {
        val requestCode = 0
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "default_channel_id"
        val channelName = "General Notifications"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.default_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}