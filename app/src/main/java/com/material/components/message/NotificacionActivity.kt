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
import java.util.concurrent.atomic.AtomicInteger
import com.squareup.okhttp.Callback
import okhttp3.ResponseBody
import com.squareup.okhttp.ResponseBody as ResponseBody1


/**
 * @author Abraham
 */
class NotificacionActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
   // lateinit var builder: Notification.Builder
    private val channelId = "com.example.vicky.notificationexample"
    private val description = "Test notification"
    private val sender_id = "343421504665"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.material.components.R.layout.activity_notificacion)
        //  notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        btn_notify.setOnClickListener {

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

    private fun sendNotificationToPatner() {



        /*responseBodyCall.enqueue(object : Callback<ResponseBody1>() {
            fun onResponse(call: retrofit2.Call<ResponseBody1>, response: retrofit2.Response<ResponseBody1>) {
                Log.d("kkkk", "done")
            }

            fun onFailure(call: retrofit2.Call<ResponseBody1>, t: Throwable) {

            }
        })
*/
    }

}
