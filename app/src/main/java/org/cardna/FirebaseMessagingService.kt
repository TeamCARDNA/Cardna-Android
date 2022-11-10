package org.cardna

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.cardna.data.local.singleton.CardNaRepository
import org.cardna.presentation.MainActivity
import timber.log.Timber


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.e("new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.e("From: " + remoteMessage.data)

        //푸시알림 ON OFF && CardNaApplication.isBackground
        if (remoteMessage.data.isNotEmpty() && CardNaRepository.pushAlarmOn) {
            Timber.e("${CardNaRepository.pushAlarmOn}")
            sendNotiNotification(remoteMessage)
        } else {
            Timber.d("pushAlarm", "Forground상태이거나 data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }

        val badgeIntent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        badgeIntent.putExtra("badge_count", 0)
        badgeIntent.putExtra("badge_count_package_name", packageName)
        badgeIntent.putExtra("badge_count_class_name", MainActivity::class.java)
        sendBroadcast(badgeIntent)
    }

    private fun sendNotiNotification(remoteMessage: RemoteMessage) {
        val uniId = remoteMessage.sentTime.toInt()

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data.getValue(key))
        }

        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent, PendingIntent.FLAG_MUTABLE
        )

        val channelId = "노티피케이션 메시지"

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_cardna_logo)
                .setLargeIcon((BitmapFactory.decodeResource(resources, R.drawable.img_logo)))
                .setContentTitle("카드나")
                .setContentText(remoteMessage.data["body"].toString())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(null)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //잠금

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId, "Notice",
                    NotificationManager.IMPORTANCE_DEFAULT,
                )
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(uniId, notificationBuilder.build())
    }
}