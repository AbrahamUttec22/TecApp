package com.material.components.activity.toolbar

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.google.firebase.auth.FirebaseAuth

import com.material.components.R
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.utils.Tools

/**
 * @author Abraham
 */
class ToolbarBasic : AppCompatActivity() {

    //instance for firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_basic)
        mAuth.signOut()
        goToActivity<LoginCardOverlap> { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
    }


}
