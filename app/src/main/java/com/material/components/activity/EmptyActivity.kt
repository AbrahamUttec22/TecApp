package com.material.components.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alejandrolora.finalapp.goToActivity
import com.google.firebase.auth.FirebaseAuth
import com.material.components.activity.login.LoginCardOverlap

/**
 * @author Abraham
 */

class MainEmptyActivity : AppCompatActivity() {

    //instance for firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // mAuth.signOut()
        //Validated for know if the user us already login
        if (mAuth.currentUser == null) {
            goToActivity<LoginCardOverlap> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            goToActivity<MainMenu> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        finish()//break
    }//end for onCreate()
}
