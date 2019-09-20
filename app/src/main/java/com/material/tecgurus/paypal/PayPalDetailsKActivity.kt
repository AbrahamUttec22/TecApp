package com.material.tecgurus.paypal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.R
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.message.ApiClient
import com.material.tecgurus.message.ApiInter
import com.material.tecgurus.message.Notification
import com.material.tecgurus.message.RequestNotificaton
import com.material.tecgurus.model.Pagos
import kotlinx.android.synthetic.main.activity_pay_pal_details_k.*
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by:
 * @author Abraham Casas Aguilar
 */
class PayPalDetailsKActivity : AppCompatActivity() {

    //declare val for save the collection
    private val empresaCollection: CollectionReference
    private val pagosCollection: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val adminCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        pagosCollection = FirebaseFirestore.getInstance().collection("Pagos")
        adminCollection = FirebaseFirestore.getInstance().collection("Administrador")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_pal_details_k)
        val intent = intent
        try {
            val jsonObject = JSONObject(intent.getStringExtra("PaymentDetails"))
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /**
     * @param response
     * @param paymentAmount
     */
    private fun showDetails(response: JSONObject, paymentAmount: String) {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd/MM/yyyy")
        val fecha = df.format(c.getTime()).toString()

        val c2 = Calendar.getInstance()
        val df2 = SimpleDateFormat("HH:mm:ss")
        val hora = df2.format(c2.getTime()).toString()//hora

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        var calendarTime = Calendar.DAY_OF_MONTH//obtener el dia del mes
        var temp = calendar.get(calendarTime)// obetnemos el dia
        calendar.set(calendarTime, temp + 30)
        val dfVencimiento = SimpleDateFormat("dd/MM/yyyy")
        val fecha_vencimiento = dfVencimiento.format(calendar.getTime()).toString()

        try {
            val id_pago = response.getString("id")
            val estatus_pago = response.getString("state")
            Log.w("DETALLESPAO", "" + response.toString())
            updateAccount(fecha, hora, paymentAmount, estatus_pago, id_pago, fecha_vencimiento)
            txtFechaPago.setText(fecha)
            txtHoraPago.setText(hora)
            txtMonto.setText("$" + paymentAmount + " MXN")
            txtFechaVencimiento.setText(fecha_vencimiento)
            fabMensual.setOnClickListener {
                goToActivity<DashboarActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    /**
     * @param fecha
     * @param hora
     * @param monto
     * @param estatus
     * @param id_pago_paypal
     * @param fecha_vencimiento
     */
    private fun updateAccount(fecha: String, hora: String, monto: String, estatus: String, id_pago_paypal: String, fecha_vencimiento: String) {
        var email_mio = mAuth.currentUser!!.email.toString()

        val consultaUsuario = empresaCollection.whereEqualTo("correo", email_mio)
        //beggin with consult
        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var id_document = ""
                var id_empresa = ""
                var nombre = ""
                for (document in task.result!!) {
                    id_document = document.id
                    id_empresa = document.get("id_empresa").toString()
                    nombre = document.get("nombre").toString()
                }
                empresaCollection.document(id_document).update("estatus", "mensual",
                        "fecha_vencimiento_plan", fecha_vencimiento).addOnSuccessListener {
                }.addOnFailureListener {
                }

                var pago = Pagos()
                pago.fecha_pago = fecha
                pago.id_empresa = id_empresa
                pago.hora_pago = hora
                pago.monto = monto
                pago.id_pago_paypal = id_pago_paypal
                pago.estatus = estatus
                pagosCollection.add(pago).addOnSuccessListener {
                    pagosCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
                }.addOnFailureListener {
                }
                sendNotificationToPatner(nombre)
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool

    }

    /**
     * @param nombre
     */
    private fun sendNotificationToPatner(nombre: String) {
        val consul = adminCollection
        //beggin with consult
        try {
            consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    var token = ""
                    for (document in task.result!!) {
                        token = document.get("token").toString()
                        val notification = Notification(nombre + " ha hecho un pago Mensual", "Empresas")
                        val requestNotificaton = RequestNotificaton()
                        //token is id , whom you want to send notification ,
                        requestNotificaton.token = token
                        requestNotificaton.notification = notification
                        val apiService = ApiClient.getClient().create(ApiInter::class.java)
                        val responseBodyCall = apiService.sendChatNotification(requestNotificaton)
                        responseBodyCall.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
                        })
                    }
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })//end for expression lambdas this very cool

        } catch (e: Exception) {

        }

    }

    private var exitTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
        } else {
        }
    }

}