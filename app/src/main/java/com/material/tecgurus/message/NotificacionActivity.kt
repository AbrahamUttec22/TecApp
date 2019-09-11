package com.material.tecgurus.message

import android.app.NotificationChannel
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_notificacion.*
import android.app.NotificationManager


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
        setContentView(com.material.tecgurus.R.layout.activity_notificacion)
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
