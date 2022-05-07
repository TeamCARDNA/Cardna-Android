package org.cardna

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import org.cardna.presentation.base.BaseViewUtil
import org.cardna.presentation.ui.alarm.view.AlarmActivity
import org.cardna.presentation.ui.detailcard.view.DetailCardActivity
import timber.log.Timber

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Timber.e("new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "ㅎㅇ")
        super.onMessageReceived(remoteMessage)
        Timber.e("From: " + remoteMessage.data)

        val title = remoteMessage.notification?.title
        val message = remoteMessage.notification?.body

        if (!message.isNullOrEmpty()) {
            Timber.e("notice 타이틀: $title")
            Timber.e("notice 바디: $message")

            if (remoteMessage.data.contains("작성")) {
                Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "작성")
                val intent = Intent(this, DetailCardActivity::class.java)
                sendNotiNotification(remoteMessage, intent)
            } else if (remoteMessage.data.contains("친구")) {
                Log.d("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ", "친구")
                val intent = Intent(this, AlarmActivity::class.java)
                sendNotiNotification(remoteMessage, intent)
            }
        } else {
            Timber.e("notice 수신에러: data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
    }

    private fun sendNotiNotification(remoteMessage: RemoteMessage, intent: Intent) {
        val uniId = remoteMessage.sentTime.toInt()

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent, PendingIntent.FLAG_MUTABLE
        )

        val channelId = "노티피케이션 메시지"

        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(remoteMessage.notification?.body.toString())
                .setContentText(remoteMessage.notification?.title.toString())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}