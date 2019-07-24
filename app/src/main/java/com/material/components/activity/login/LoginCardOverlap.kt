package com.material.components.activity.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import com.alejandrolora.finalapp.isValidEmail
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login_card_overlap.*
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.OnCompleteListener
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.validate
import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.activity.dialog.*
import com.material.components.model.Encuesta
import com.material.components.register.RegistrosActivity
import kotlinx.android.synthetic.main.activity_agregar_encuesta.*
import kotlinx.android.synthetic.main.activity_expansion_panel_basic.*
import kotlinx.android.synthetic.main.dialog_code.*
import android.content.SharedPreferences as SharedPreferences1

/**
 * @author Abraham
 */
class LoginCardOverlap : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //declare val for save the collection
    private val userCollection: CollectionReference

    //declare val for save the collection
    private val codeCollection: CollectionReference
    //declare val for save the collection
    private val empresaCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        codeCollection = FirebaseFirestore.getInstance().collection("registros")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
    }

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.material.components.R.layout.activity_login_card_overlap)
        //listener
        buttonLogIn.setOnClickListener {
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            if (isValidEmail(email) && !password.isNullOrEmpty()) {//valid the params
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = ""
                builder.setView(dialogView)
                logInByEmail(email, password)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                Handler().postDelayed({ dialog.dismiss() }, 1000)
            } else {
                toast("Completa los campos")
            }
        }//end for listener

        //this I need validated a code of access, this code the admin can update later
        buttonSignUp.setOnClickListener {
            //showDialogAbout()
            goToActivity<RegistrosActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }// end for listener register
    }//end for onCreate

    /**
     * @param email
     * @param password
     * @return void
     */
    private fun logInByEmail(email: String, password: String) {
        //primero inicio sesion con las credenciales, despues valido si es empresa o empleado
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {//when the credentials are corrects
                if (mAuth.currentUser!!.isEmailVerified) {
                    val empleado = userCollection.whereEqualTo("email", email)
                    //beggin with consult
                    empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                val rol = document.get("rol").toString()
                                val id_empresa = document.get("id_empresa").toString()
                                if (rol!! == "administrador") {
                                    val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                    var sesion = sharedPreference.edit()
                                    sesion.putString("id_empresa", id_empresa)
                                    sesion.commit()
                                    goToActivity<MainMenu> {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                } else {
                                    val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                    var sesion = sharedPreference.edit()
                                    sesion.putString("id_empresa", id_empresa)
                                    sesion.commit()
                                    goToActivity<MainMenu> {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                }
                            }
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool
                    //case for empresa
                    val empresa = empresaCollection.whereEqualTo("correo", email)
                    empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            for (document in task.result!!) {
                                //here i send the id_empresa
                                val id_empresa = document.get("id_empresa").toString()
                                val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                var sesion = sharedPreference.edit()
                                sesion.putString("id_empresa", id_empresa)
                                sesion.commit()
                                goToActivity<MainMenu> {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                            }
                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }
                    })//end for expression lambdas this very cool
                } else {
                    toast("Confirma tu cuenta, se envio un correo con el que te registraste a tu bandeja")
                }
            } else {
                toast("Email o Contraseña incorrectas intenta de nuevo")
            }
        }
    }

    //I need valid the access
    private fun showDialogAbout() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_code)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_Cancel) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
        }

        (dialog.findViewById<View>(R.id.bt_Accept) as AppCompatButton).setOnClickListener { v ->
            val code = (dialog.findViewById<View>(R.id.txtCodeAccess) as EditText).text.toString()
            val editText = (dialog.findViewById<View>(R.id.txtCodeAccess) as EditText)
            if (code.isNullOrEmpty()) {
                editText?.error = "Ingresa el codigo"
            } else {
                validCode(code, dialog)
            }

        }
        dialog.show()
        dialog.window!!.attributes = lp

    }

    /**
     * @param code
     * @return void
     */
    private fun validCode(code: String, dialog: Dialog) {
        val resultado = codeCollection.whereEqualTo("acceso", code)
        //beggin with consult
        resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val acceso = document.get("acceso").toString()
                    if (acceso.equals(code)) {
                        goToActivity<SignUpActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    } else {
                        val editText = (dialog.findViewById<View>(R.id.txtCodeAccess) as EditText)
                        editText?.error = "Codigo incorrecto"
                    }
                }
            } else {
                Log.w("EXCEPTION", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
    }

}