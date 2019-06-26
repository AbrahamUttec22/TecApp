package com.material.components.activity.login

import android.app.Dialog
import android.content.Intent
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
import com.alejandrolora.finalapp.goToActivity
import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.activity.dialog.*


/**
 * @author Abraham
 */
class LoginCardOverlap : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //declare val for save the collection
    private val userCollection: CollectionReference
    private val registrosCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        registrosCollection = FirebaseFirestore.getInstance().collection("registros")
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

        buttonSignUp.setOnClickListener {
            val codi="Guerrero@g"
            accessToSignUp(codi)
        }// end for listener register
    }//end for onCreate

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
                })
            } else {
                toast("Email o Contraseña incorrectas intenta de nuevo")
            }
        }
    }


    private fun accessToSignUp(codigo: String) {
        registrosCollection.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val cod = document.get("acceso").toString()
                    if (cod!! == codigo) {
                        goToActivity<SignUpActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    } else {
                        toast("Codigo incorrecto")
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })
    }


}
