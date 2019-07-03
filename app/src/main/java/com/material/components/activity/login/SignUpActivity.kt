package com.material.components.activity.login

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.alejandrolora.finalapp.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.model.Usuario
import kotlinx.android.synthetic.main.activity_sign_up.*

/**
 * @author Abraham
 */
class SignUpActivity : AppCompatActivity() {

    //declare val for save the collection
    private val marksCollection: CollectionReference

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //the image for default
        val imag = "https://firebasestorage.googleapis.com/v0/b/tecapp-25ed3.appspot.com/o/usuarios%2Fic_default_user.png?alt=media&token=7e64e478-9802-4149-915a-dba6d3c69c59"//this is a image for default
        //on listener for the buton on register a new user
        registrarUsuario.setOnClickListener {
            val name = txtName.text.toString()
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val confirmpassword = txtConfirmPassword.text.toString()
            if (isValid(name, email) && isValidEmail(email) && isValidConfirmPassword(password, confirmpassword)) {
                val usuario = Usuario()
                usuario.name = name
                usuario.email = email
                usuario.ubicacion = imag
                usuario.edad = ""
                usuario.telefono = ""
                usuario.direccion = ""
                if (radio_adminitrador.isChecked)
                    usuario.rol = "administrador"
                if (radio_usuario.isChecked)
                    usuario.rol = "usuario"
                //first save the user on authentication firebase, after that save the user on cloud firestore
                signUpByEmail(email, password, usuario)
            } else {
                toast("Completa los campos")
            }
        }//end for listner

        txtEmail.validate {
            txtEmail.error = if (isValidEmail(it)) null else "El email no es valido"
        }
        txtConfirmPassword.validate {
            txtConfirmPassword.error = if (isValidConfirmPassword(txtPassword.text.toString(), it)) null else "Las cotraseñas no coinciden"
        }

        buttonBackLogin.setOnClickListener {
            goToActivity<LoginCardOverlap> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }//end for to back login
    }//end handler


    //fun for backend
    /**
     * @param email
     * @param password
     * @param usuario
     * in this handler the register on authentication with firebase
     */
    private fun signUpByEmail(email: String, password: String, usuario: Usuario) {
        //get instance of firebase

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                //save the user on cloud firestore
                // mAuth.signOut()//this is necesary because the val is in general
                saveUser(usuario)
            } else {
                // toast("Los Datos ingresados ya estan registrados,intenta con uno nuevo")
                showErrorDialog()
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
            showConfirmDialog()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el usuario, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    private fun isValid(name: String, email: String): Boolean {
        return !name.isNullOrEmpty() &&
                !email.isNullOrEmpty() &&
                !txtPassword.text.isNullOrEmpty() &&
                !txtConfirmPassword.text.isNullOrEmpty()
    }

    //clean the text
    private fun setEmpty() {
        txtConfirmPassword.setText("")
        txtPassword.setText("")
        txtName.setText("")
        txtEmail.setText("")
    }

    //this is the dialog confirm, afther that register a new user
    private fun showConfirmDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_info)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
            setEmpty()
            mAuth.signOut()
        }

        (dialog.findViewById<View>(R.id.bt_back_login) as AppCompatButton).setOnClickListener { v ->
            setEmpty()
            dialog.dismiss()
            mAuth.signOut()
            goToActivity<LoginCardOverlap> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    //this is the dialog confirm, afther that register a new user
    private fun showErrorDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_warning)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
            mAuth.signOut()
        }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    override fun onBackPressed() {
        doExitApp()
    }

    fun doExitApp() {
    }


}