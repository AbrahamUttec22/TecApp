package com.material.components.activity.card

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

import com.material.components.R
import com.material.components.model.Usuario
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_card_overlaps.*

/**
 * @author  Abraham
 * 28/06/2019
 */
class CardOverlap : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_overlaps)
        sendDta()
        initToolbar()
    }

    /**
     * send information to view Mi perfil
     */
    private fun sendDta(){
        if(mAuth.currentUser!=null){
            var email= mAuth.currentUser!!.email.toString()
            val resultado = userCollection.whereEqualTo("email", email)
            //beggin with consult
            resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val nombre = document.get("name").toString()
                        val edad = document.get("edad").toString()
                        val telefono = document.get("telefono").toString()
                        val email = document.get("email").toString()
                        val direccion = document.get("direccion").toString()
                        //nombre
                        txtNombre.text=nombre
                        //edad
                        txtEdad.text=edad
                        //telefono
                        txtTelefono.text=telefono
                        //email
                        txtEmail.text=email
                        //direccion
                        txtDireccion.text=direccion
                    }
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })
        }
    }



    //unique views
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_basic, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
