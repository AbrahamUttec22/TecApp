package com.material.components.message

import android.app.NotificationChannel
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.material.components.R
import android.app.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.RemoteViews
import com.alejandrolora.finalapp.toast
import com.google.firebase.messaging.FirebaseMessaging
import com.material.components.activity.MainMenu
import kotlinx.android.synthetic.main.activity_notificacion.*
import kotlin.random.Random
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author Abraham
 */
class NotificacionActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "com.example.vicky.notificationexample"
    private val description = "Test notification"
    private val sender_id = "343421504665"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacion)
        //  notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        btn_notify.setOnClickListener {
            val mBuilder: NotificationCompat.Builder
            val mNotifyMgr = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val icono = R.mipmap.ic_launcher
            val i = Intent(this, MainMenu::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, i, 0)
            mBuilder = NotificationCompat.Builder(applicationContext,channelId)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icono)
                    .setContentTitle("Titulo")
                    .setContentText("Hola que tal?")
                    .setVibrate(longArrayOf(100, 250, 100, 500))
                    .setAutoCancel(true)
            mNotifyMgr.notify(1, mBuilder.build())

        }
        /* val fm = FirebaseMessaging.getInstance()
         val message = RemoteMessage.Builder(sender_id + "@gcm.googleapis.com")
                 .setMessageId(Integer.toString(Random.nextInt(0, 100)))
                 .addData("UNO", "UNOD")
                 .addData("DOS", "DOSD")
                 .build()
         if (!message.data.isEmpty()) {
             Log.e("CLOUDD", "UpstreamData: " + message.data)
         }

         if (!message.messageId!!.isEmpty()) {
             Log.e("CLOUDD", "UpstreamMessageId: " + message.messageId)
         }
         fm.send(message)*/
    }
}
