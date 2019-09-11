package com.material.tecgurus.activity.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.isValidEmail
import com.alejandrolora.finalapp.toast
import com.alejandrolora.finalapp.validate
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.material.tecgurus.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

/**
 * @author ABRAHAM CASAS AGUILAR
 */
class ForgotPasswordActivity : AppCompatActivity() {
    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        buttonRecoverPassword.setOnClickListener {
            val email = txtEmailForgotPassword.text.toString()
            if (isValid(email) && isValidEmail(email)) {
                recoverPassword(email)
            } else {
                toast("Ingresa un correo")
            }
        }
        txtEmailForgotPassword.validate {
            txtEmailForgotPassword.error = if (isValidEmail(it)) null else "El email no es valido"
        }
    }

    private fun recoverPassword(email: String) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Se te ha enviado un correo, para recuperar tu contraseña", Toast.LENGTH_LONG).show()
                goToActivity<LoginCardOverlap> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                Toast.makeText(this, "Se te ha enviado un correo, para recuperar tu contraseña", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Se te ha enviado un correo, para recuperar tu contraseña", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        goToActivity<LoginCardOverlap> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

    private fun isValid(password: String): Boolean {
        return !password.isNullOrEmpty()
    }
}
