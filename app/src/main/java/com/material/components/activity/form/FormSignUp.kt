package com.material.components.activity.form

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.material.components.R
import com.material.components.activity.bottomsheet.BottomSheetFull
import com.material.components.model.People
import com.material.components.model.Usuario
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_form_sign_up.*

/**
 * @author Abraham
 */
class FormSignUp : AppCompatActivity() {


    //declare val for save the collection
    private val marksCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_sign_up)
        initToolbar()
        val imag = 6//this is a image for default
        //on listener for the buton on register a new user
        registrarUsuario.setOnClickListener {

            val name = txtName.text.toString()
            val email = txtEmail.text.toString()
            val password=txtPassword.text.toString()
            val confirmpassword=txtConfirmPassword.text.toString()
            if (isValid(name, email)&& isValidEmail(email) && isValidConfirmPassword(password, confirmpassword) ) {
                val usuario = Usuario()
                usuario.name = name
                usuario.email = email
                usuario.image = imag
                if (radio_adminitrador.isChecked)
                    usuario.rol = "administrador"
                if (radio_usuario.isChecked)
                    usuario.rol = "usuario"
                //first save the user on authentication firebase, after that save the user on cloud firestore
                signUpByEmail(email,password,usuario)

            } else {
                toast("Completa los campos")
            }
        }// end for listener

        txtEmail.validate {
            txtEmail.error = if (isValidEmail(it)) null else "El email no es valido"
        }

        txtConfirmPassword.validate {
            txtConfirmPassword.error = if (isValidConfirmPassword(txtPassword.text.toString(), it)) null else "Las cotraseñas no coinciden"
        }
    }

    //fun for backend
    /**
     * @param email
     * @param password
     * @param usuario
     * in this handler the register on authentication with firebase
     */
    private fun signUpByEmail(email: String, password: String,usuario:Usuario) {
        //get instance of firebase
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isCanceled) {
                //save the user on cloud firestore
                saveUser(usuario)
            } else {
                toast("Los Datos ingresados ya estan registrados,intenta con uno nuevo")
            }
        }
    }

    /**
     * @param usuario
     * in this handler the save user on cloud firestore on the collection with name Usuarios
     */
    private fun saveUser(usuario: Usuario) {
        //add the collection and save the User, this is validated
        marksCollection.add(usuario).addOnSuccessListener {
            Toast.makeText(this, "Usuario registrado con exito", Toast.LENGTH_LONG).show()
         /*   val intento1 = Intent(this, BottomSheetFull::class.java)
            startActivity(intento1)*/
            //startActivity(Intent(this, BottomSheetFull::class.java))
           onBackPressed()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el usuario, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    //fun for view
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Registrar Usuario")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //startActivity(Intent(this, BottomSheetFull::class.java))
            onBackPressed()
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValid(name: String, email: String): Boolean {
        return !name.isNullOrEmpty() &&
                !email.isNullOrEmpty() &&
                !txtPassword.text.isNullOrEmpty() &&
                !txtConfirmPassword.text.isNullOrEmpty()
    }


}
