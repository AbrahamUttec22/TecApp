package com.material.components.checador

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.itextpdf.xmp.impl.Base64
import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.drawer.DashboarActivity
import com.material.components.model.Checador
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_check.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham Casas Aguilar
 */
class CheckActivity : AppCompatActivity() {

    private val checadorCollection: CollectionReference
    private val usuariosCollection: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var scannedResult: String = ""


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        checadorCollection = FirebaseFirestore.getInstance().collection("Checador")
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        initToolbar()
        //I open the camera for capture the code
        btnScan.setOnClickListener {
            run {
                IntentIntegrator(this).initiateScan()
            }
        }
    }

    /**
     * afther thar capture the information
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                var correo = mAuth.currentUser!!.email.toString()
                val empleado = usuariosCollection.whereEqualTo("email", correo)
                //beggin with consult
                empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            try {
                                var id_empresa = document.get("id_empresa").toString()
                                scannedResult = result.contents
                                var scannedResult64 = Base64.decode(scannedResult)
                                var cadena = scannedResult64.substring(0, scannedResult64.length - 18)//id empresa del QR
                                var horaQR = scannedResult64.substring(cadena.length, scannedResult64.length - 10)//hora del QR
                                var sumacadena = cadena + horaQR
                                var fechaQR = scannedResult64.substring(sumacadena.length, scannedResult64.length)//hora del QR

                                val c = Calendar.getInstance()
                                val df = SimpleDateFormat("dd/MM/yyyy")
                                val formattedDate = df.format(c.getTime()).toString()
                                val c2 = Calendar.getInstance()
                                val df2 = SimpleDateFormat("HH:mm:ss")
                                val formattedDate2 = df2.format(c2.getTime()).toString()//hora

                                //4:31:01  + 64 si
                                //         60 es > a 59 y 60 es < 64que la suma de 5 segundos
                                var horaQ = horaQR.substring(0, 2).toInt()//HH
                                var minutoQ = horaQR.substring(3, 5).toInt()//mm
                                var segundoQ = horaQR.substring(6, 8).toInt()//ss

                                //4:31:06
                                var horaBd = formattedDate2.substring(0, 2).toInt()
                                var minutoDB = formattedDate2.substring(3, 5).toInt()
                                var segundoDB = formattedDate2.substring(6, 8).toInt()


                                if (id_empresa.equals(cadena)) {
                                    if (horaQ == horaBd && fechaQR.equals(formattedDate)) {
                                        if (minutoQ == minutoDB) {
                                            var segundosumado = segundoQ + 10
                                            if (segundoDB >= segundoQ && segundoDB <= segundosumado) {
                                                var checador = Checador()
                                                checador.fecha = formattedDate
                                                checador.hora = formattedDate2
                                                checador.id_empresa = cadena
                                                checador.id_usuario = document.get("id").toString()
                                                checador.nombre = document.get("name").toString()
                                                checador.id = ""
                                                saveChecador(checador)
                                                showConfirmDialog()
                                            } else {
                                                showErrorDialog()
                                            }
                                        } else if (fechaQR.equals(formattedDate)) {
                                            var minutomasuno = minutoQ + 1
                                            if (minutomasuno == minutoDB) {
                                                var resta = segundoQ - segundoDB // 55 55 55
                                                if (resta == 53 || resta == 54 || resta == 55 || resta == 56 || resta == 57 || resta == 58 || resta == 59 || resta == 60) {
                                                    var checador = Checador()
                                                    checador.fecha = formattedDate
                                                    checador.hora = formattedDate2
                                                    checador.id_empresa = cadena
                                                    checador.id_usuario = document.get("id").toString()
                                                    checador.nombre = document.get("name").toString()
                                                    checador.id = ""
                                                    saveChecador(checador)
                                                    showConfirmDialog()
                                                } else {
                                                    showErrorDialog()
                                                }
                                            } else {
                                                showErrorDialog()
                                            }
                                        }
                                    } else {
                                        showErrorDialog()
                                    }
                                } else {
                                    showErrorDialog()
                                }
                            }
                            catch (e: Exception) {
                                // handler
                                showErrorDialog()

                            }

                        }
                    } else {
                        Log.w("saasas", "Error getting documents.", task.exception)
                    }
                })//end for expression lambdas this very cool
            } else {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    //may the qr failed, I prepared for that with two handlers
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult")


        }
    }


    /**
     * @param evento
     * in this handler the save evento on cloud firestore on the collection with name Eventos
     */
    private fun saveChecador(checador: Checador) {
        //add the collection and save the User, this is validated
        checadorCollection.add(checador).addOnSuccessListener {
            checadorCollection.document(it.id).update("id", it.id).addOnSuccessListener {}.addOnFailureListener { }
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el evento, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    //THE DIALOGS FOR EVERY CASE

    //this is the dialog confirm, afther that register a new user
    private fun showErrorDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_warning_checador)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    //this is the dialog confirm, afther that register a new user
    private fun showConfirmDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_info_checador)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Checador"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}