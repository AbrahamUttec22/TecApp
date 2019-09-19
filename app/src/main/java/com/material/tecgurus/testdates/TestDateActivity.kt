package com.material.tecgurus.testdates

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
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
import kotlinx.android.synthetic.main.activity_dashboard_administrador.*
import kotlinx.android.synthetic.main.activity_dashboard_empresa.*
import kotlinx.android.synthetic.main.activity_dashboard_usuario.*
import kotlinx.android.synthetic.main.activity_test_date.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

/**
 * @pruebas
 */
class TestDateActivity : AppCompatActivity() {

    private val empresaCollection: CollectionReference

    init {
        FirebaseApp.initializeApp(this)
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
    }

    /**
     * Esta clase sirve para hacer pruebas de fechas, y evitar llegar a un proceso largo,
     * simplemente lo hago dando click en un boton para comprobar si el codigo es correcto
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_date)
        buttonPruebas.setOnClickListener {
        }
    }//end for onCreate()

    //Pruebas pasadas con exito
    private fun validarPlanMensual() {
        var id_empresa = ""
        val empresaEstatus = empresaCollection.whereEqualTo("id_empresa", id_empresa)
        empresaEstatus.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var FECHA_VENCIMIENTO = ""
                var id = ""
                for (document in task.result!!) {
                    FECHA_VENCIMIENTO = document.get("fecha_vencimiento_plan").toString()
                    id = document.id
                }

                val c = Calendar.getInstance()
                val df = SimpleDateFormat("dd/MM/yyyy")
                val FECHA_HOY = df.format(c.getTime()).toString()

                var dia_vencimiento = FECHA_VENCIMIENTO.substring(0, 2).toInt()//dd
                var mes_vencimiento = FECHA_VENCIMIENTO.substring(3, 5).toInt()//mm
                var ano_vencimiento = FECHA_VENCIMIENTO.substring(6, 10).toInt()//yyyy


                var dia_hoy = FECHA_HOY.substring(0, 2).toInt()//dd
                var mes_hoy = FECHA_HOY.substring(3, 5).toInt()//mm
                var ano_hoy = FECHA_HOY.substring(6, 10).toInt()//yyyy

                var cont = 0
                if (mes_vencimiento == mes_hoy && ano_vencimiento == ano_hoy) {
                    if (dia_vencimiento >= dia_hoy) {
                        cont++
                        //plan sigue activo
                        toast("PLAN SIGUE ACTIVO")
                    } else {
                        cont++
                        //plan se acabo
                        toast("PLAN SE ACABO")
                        //only this source I update the status,
                        empresaCollection.document(id).update("estatus", "gratuita").addOnSuccessListener {
                        }.addOnFailureListener {
                        }
                    }
                } else if (ano_vencimiento > ano_hoy) {
                    cont++
                    //plan sigue activo
                    toast("PLAN SIGUE ACTIVO")
                }

                if (cont == 0) {
                    if (mes_vencimiento > mes_hoy && ano_vencimiento >= ano_hoy) {
                        //sigue siendo valido
                        toast("PLAN SIGUE ACTIVO")
                    } else if (mes_vencimiento == 1 && mes_hoy == 12 && ano_vencimiento > ano_hoy) {
                        //sigue siendo valido
                        toast("PLAN SIGUE ACTIVO")
                    } else {
                        //plan se acabo
                        toast("PLAN SE ACABO")
                        empresaCollection.document(id).update("estatus", "gratuita").addOnSuccessListener {
                        }.addOnFailureListener {
                        }
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }

    /**
     * VALIDAR MENSUALIDAD
     * 30-12=
     * 12-12=0  pero el mes no es el mismo no vale
     * 11-22=-1 pero el mes no es el mismo no vale
     *
     *
     * PRUEBAS : EN ESTE MISMO MES ACABE
     * FECHA VENCIMIENTO= 05/01/2020
     * FECHA HOY=         05/12/2019
     *
     * ano_vencimiento=2020
     * mes_vencimiento=01
     * dia_venciiento=05
     *
     * mes_hoy=12
     * ano_hoy=2019
     * dia_hoy=31
     *
     * if(mes_vencimiento==mes_hoy && ano_vencimiento==ano_hoy){
     *    if(dia_vencimiento>= dia_hoy){
     *    //plan sigue activo
     *    }else{
     *    //plan se acabo
     *    }
     * }
     *
     * if(mes_vencimiento > mes_hoy && ano_vencimiento>=ano_hoy ){
     *  sigue siendo valido
     * }else if(mes_vencimiento==01 && mes_hoy==12 && ano_vencimiento>ano_hoy){
     * sigue siendo valido
     * }else{
     * //plan se acabo
     *
     * }
     *
     */

}