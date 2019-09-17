package com.material.tecgurus.activity.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import android.widget.TextView
import com.alejandrolora.finalapp.goToActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.material.tecgurus.R
import com.material.tecgurus.admin.AdminDashboardActivity
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.register.RegistrosActivity

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
    //declare val for save the collection
    private val adminCollection: CollectionReference
    lateinit var dialog: AlertDialog

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        codeCollection = FirebaseFirestore.getInstance().collection("registros")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        adminCollection = FirebaseFirestore.getInstance().collection("Administrador")
    }

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.material.tecgurus.R.layout.activity_login_card_overlap)
        //listener
        buttonLogIn.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString()
            if (isValidEmail(email) && !password.isNullOrEmpty()) {//valid the params
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = ""
                builder.setView(dialogView)
                logInByEmail(email, password)
                builder.setCancelable(false)
                dialog = builder.create()
                dialog.show()

                //Handler().postDelayed({ dialog.dismiss() }, 1400)
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
        forgot_password.setOnClickListener {
            goToActivity<ForgotPasswordActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
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

                    val admin = adminCollection.whereEqualTo("correo", email)
                    admin.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var conAdmin = 0
                            for (document in task.result!!) {
                                conAdmin++
                                //here i send the id_empresa
                                val token = FirebaseInstanceId.getInstance().token.toString()
                                val id = document.id
                                adminCollection.document(id).update("token", token).addOnSuccessListener {
                                //    toast("" + token)
                                }.addOnFailureListener {}
                                dialog.dismiss()
                                goToActivity<AdminDashboardActivity> {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            }

                        } else {
                            Log.w("saasas", "Error getting documents.", task.exception)
                        }

                    })//end for expression lambdas this very cool

                    val empleado = userCollection.whereEqualTo("email", email)
                    //beggin with consult
                    empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var conEmpleado = 0
                            for (document in task.result!!) {
                                conEmpleado++
                                val rol = document.get("rol").toString()
                                val id_empresa = document.get("id_empresa").toString()
                                val estatus = document.get("estatus").toString()
                                val id = document.id
                                if (estatus.equals("1")) {
                                    if (rol == "administrador") {
                                        val token = FirebaseInstanceId.getInstance().token.toString()
                                        userCollection.document(id).update("token", token).addOnSuccessListener {
                                        }.addOnFailureListener {}
                                        userCollection.document(id).update("id", id).addOnSuccessListener {
                                        }.addOnFailureListener {}
                                        val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                        var sesion = sharedPreference.edit()
                                        sesion.putString("id_empresa", id_empresa)
                                        sesion.putString("rol", "administrador")
                                        sesion.putString("token", token)
                                        sesion.commit()
                                        dialog.dismiss()
                                        goToActivity<DashboarActivity> {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        }
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                    } else {
                                        val token = FirebaseInstanceId.getInstance().token.toString()
                                        userCollection.document(id).update("token", token).addOnSuccessListener {
                                        }.addOnFailureListener {}
                                        userCollection.document(id).update("uid", mAuth.uid.toString()).addOnSuccessListener {
                                        }.addOnFailureListener {}
                                        val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                        var sesion = sharedPreference.edit()
                                        sesion.putString("id_empresa", id_empresa)
                                        sesion.putString("rol", "usuario")
                                        sesion.putString("token", token)
                                        sesion.commit()
                                        dialog.dismiss()
                                        goToActivity<DashboarActivity> {
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        }
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                    }
                                } else {
                                    mAuth.signOut()
                                    toast("Email o Contraseña incorrectas intenta de nuevo")
                                    dialog.dismiss()
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
                            var conEmpresa = 0
                            for (document in task.result!!) {
                                conEmpresa++
                                //here i send the id_empresa
                                val id_empresa = document.get("id_empresa").toString()
                                val token = FirebaseInstanceId.getInstance().token.toString()
                                val id = document.id
                                empresaCollection.document(id).update("token", token).addOnSuccessListener {
                                }.addOnFailureListener {}
                                val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                                var sesion = sharedPreference.edit()
                                sesion.putString("id_empresa", id_empresa)
                                sesion.putString("rol", "empresa")
                                sesion.putString("token", token)
                                sesion.commit()
                                dialog.dismiss()
                                goToActivity<DashboarActivity> {
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
                    dialog.dismiss()
                }
            } else {
                toast("Email o Contraseña incorrectas intenta de nuevo")
                dialog.dismiss()
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
                        editText.error = "Codigo incorrecto"
                    }
                }
            } else {
                Log.w("EXCEPTION", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
    }

}