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

        //푸시알림 ON OFF저장해야함  && CardNaApplication.isBackground
        if (remoteMessage.data.isNotEmpty() && CardNaApplication.isBackground && CardNaRepository.pushAlarmOn) {
            Timber.e("${CardNaRepository.pushAlarmOn}")
            sendNotiNotification(remoteMessage)
        } else {
            Timber.d("pushAlarm", "Forground상태이거나 data가 비어있습니다. 메시지를 수신하지 못했습니다.")
        }
    }

    private fun sendNotiNotification(remoteMessage: RemoteMessage) {
        val uniId = remoteMessage.sentTime.toInt()

        //intent생성
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        //각 key, value 추가
        for (key in remoteMessage.data.keys) {
            intent.putExtra(key, remoteMessage.data.getValue(key))
        }

        val pendingIntent = PendingIntent.getActivity(
            this, uniId, intent, PendingIntent.FLAG_MUTABLE
        )

        // 알림 채널 이름
        val channelId = "노티피케이션 메시지"

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(
                    (BitmapFactory.decodeResource(resources, R.drawable.img_logo))
                )
                .setContentTitle("카드나")
                .setContentText(remoteMessage.data["body"].toString())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(null) //소리
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //잠금
                //    .setNumber(1) //배지 갯수 넣는부분
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL) //배지 스타일을 이렇게 주어야한다.

        //     ShortcutBadger.applyCount(applicationContext, 1)// <--해당부분을 통해 배지 갯수가 표시된다


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