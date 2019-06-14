package com.material.components.activity.login

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.isValidEmail
import com.alejandrolora.finalapp.toast
import com.google.firebase.auth.FirebaseAuth

import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_login_card_overlap.*

/**
 * @author Abraham
 */
class LoginCardOverlap : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_card_overlap)
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

    }//end for onCreate

    /**
     * @param email
     * @param password
     */
    private fun logInByEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {//when the credentials are corrects
                goToActivity<MainMenu> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            } else {
                toast("Email o Contraseña incorrectas intenta de nuevo")
            }
        }
    }

}
