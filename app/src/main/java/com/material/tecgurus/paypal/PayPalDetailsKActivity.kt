package com.material.tecgurus.paypal
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.common.data.DataBufferObserverSet
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.R
import com.material.tecgurus.actividadesfragment.GestionActividadesActivity
import com.material.tecgurus.actividadesfragmentadmin.GestionActividadesAActivity
import com.material.tecgurus.activity.about.EstatusChecadorActivity
import com.material.tecgurus.activity.bottomsheet.UserActivity
import com.material.tecgurus.activity.card.*
import com.material.tecgurus.activity.dialog.EncuestaActivity
import com.material.tecgurus.activity.form.*
import com.material.tecgurus.activity.login.LoginCardOverlap
import com.material.tecgurus.checador.CheckActivity
import com.material.tecgurus.checador.GenerarQrJActivity
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.model.Pagos
import com.material.tecgurus.model.Usuario
import kotlinx.android.synthetic.main.activity_dashboard_administrador.*
import kotlinx.android.synthetic.main.activity_dashboard_empresa.*
import kotlinx.android.synthetic.main.activity_dashboard_usuario.*
import kotlinx.android.synthetic.main.activity_pay_pal_details_k.*
import org.json.JSONException
import org.json.JSONObject
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

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        pagosCollection = FirebaseFirestore.getInstance().collection("Pagos")
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

    private fun showDetails(response: JSONObject, paymentAmount: String) {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd/MM/yyyy")
        val fecha = df.format(c.getTime()).toString()

        val c2 = Calendar.getInstance()
        val df2 = SimpleDateFormat("HH:mm:ss")
        val hora = df2.format(c2.getTime()).toString()//hora

        val c3 = Calendar.getInstance()
        val df3 = SimpleDateFormat("dd/MM/yyyy")
        val fecha_vencimiento = df3.format(c3.getTime()).toString()

        try {
            val id_pago = response.getString("id")
            val estatus_pago = response.getString("state")
            Log.w("DETALLESPAO", "" + response.toString())
            updateAccount(fecha, hora, paymentAmount, estatus_pago, id_pago, fecha_vencimiento)
            txtFechaPago.setText(fecha)
            txtHoraPago.setText(hora)
            txtMonto.setText("$" + paymentAmount + " MXN")
            fab.setOnClickListener {
                goToActivity<DashboarActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun updateAccount(fecha: String, hora: String, monto: String, estatus: String, id_pago_paypal: String, fecha_vencimiento: String) {
        var email_mio = mAuth.currentUser!!.email.toString()

        val consultaUsuario = empresaCollection.whereEqualTo("correo", email_mio)
        //beggin with consult
        consultaUsuario.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var id_document = ""
                var id_empresa = ""
                for (document in task.result!!) {
                    id_document = document.id
                    id_empresa = document.get("id_empresa").toString()
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
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool

    }

    private var exitTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
        } else {
        }
    }
}