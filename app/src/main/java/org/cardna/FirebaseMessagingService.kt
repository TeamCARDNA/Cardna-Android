package org.cardna

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.cardna.presentation.MainActivity
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.e("new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Timber.e("From: " + remoteMessage.data)

        //푸시알림 ON OFF저장해야함  && CardNaApplication.isBackground
        if (remoteMessage.data.isNotEmpty() && CardNaApplication.isBackground) {
            sendNotiNotification(remoteMessage)
            Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${remoteMessage.data["title"].toString()}")
            Timber.e("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ${remoteMessage.data["body"].toString()}")
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
        // .setNumber(6)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId, "Notice",
                    NotificationManager.IMPORTANCE_DEFAULT,
                    //NotificationManager.IMPORTANCE_LOW
                )
              channel.vibrationPattern = longArrayOf(0) // 진동 끄기
              channel.enableVibration(false) // 진동 끄기*/

            channel.vibrationPattern = longArrayOf(100, 200) // 진동주기

            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(uniId, notificationBuilder.build())
    }



/*    fun getBitmapCircleCrop(bitmap: Bitmap, Width: Int = 0, Height: Int = 0): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        paint.setColor(color)
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        var CroppedBitmap = output
        //width, Height에 0,0을 넣으면 원본 사이즈 그대로 출력
        if (Width != 0 && Height != 0) CroppedBitmap = Bitmap.createScaledBitmap(output, Width, Height, false)
        return CroppedBitmap
    }*/

}