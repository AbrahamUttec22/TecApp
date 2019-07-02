package com.material.components.activity.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_expansion_panel_basic.*
import kotlinx.android.synthetic.main.dialog_code.*


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

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        codeCollection = FirebaseFirestore.getInstance().collection("registros")
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
                logInByEmail(email, password)
            } else {
                toast("Completa los campos")
            }
        }//end for listener

        //this I need validated a code of access, this code the admin can update later
        buttonSignUp.setOnClickListener {
            showDialogAbout()
        }// end for listener register
    }//end for onCreate

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
            val editText= (dialog.findViewById<View>(R.id.txtCodeAccess) as EditText)
            if (code.isNullOrEmpty()) {
                editText?.error = "Ingresa el codigo"
            }else{
                validCode(code,dialog)
            }

        }
        dialog.show()
        dialog.window!!.attributes = lp

    }

    private fun isValid(code: String): Boolean {
        return !code.isNullOrEmpty()
    }


    /**
     * @param code
     * @return void
     */
    private fun validCode(code: String,dialog:Dialog) {
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
                        val editText= (dialog.findViewById<View>(R.id.txtCodeAccess) as EditText)
                            editText?.error = "Codigo incorrecto"
                    }
                }
            } else {
                Log.w("EXCEPTION", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
    }



    /**
     * @param email
     * @param password
     * @return void
     */
    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {//when the credentials are corrects
                val resultado = userCollection.whereEqualTo("email", email)
                //beggin with consult
                resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val rol = document.get("rol").toString()
                            if (rol!! == "administrador") {
                                goToActivity<MainMenu> {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            } else {
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
            } else {
                toast("Email o Contraseña incorrectas intenta de nuevo")
            }
        }
    }


}
