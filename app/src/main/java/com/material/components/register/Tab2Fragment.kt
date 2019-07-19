package com.material.components.register

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.model.Usuario
import kotlinx.android.synthetic.main.tab1_fragment.view.btnRegistrarEmpresa
import kotlinx.android.synthetic.main.tab2_fragment.view.*
import java.util.regex.Pattern
import android.content.Context
import android.os.Handler
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.activity.MainMenu

/**
 * @author Abraham
 * Empresa
 */
class Tab2Fragment : Fragment() {
    //declare val for save the collection
    //declare val for save the collection
    private val usuariosCollection: CollectionReference
    //declare val for save the collection
    private val empresasCollection: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var status: Boolean = false

    //init the val for get the collection the Firebase with cloud firestore
    init {
        //save the collection marks on val maksCollection
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        empresasCollection = FirebaseFirestore.getInstance().collection("Empresas")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tab2_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //the image for default
        val imag = "https://firebasestorage.googleapis.com/v0/b/tecapp-25ed3.appspot.com/o/usuarios%2Fic_default_user.png?alt=media&token=7e64e478-9802-4149-915a-dba6d3c69c59"//this is a image for default
        //on listener for the buton on register a new user
        view.btnRegistrarEmpleado.setOnClickListener {
            //direction foto and id_empresa is empty first
            val name = view.txtNombreEmpleado.text.toString()
            val codigo = view.txtCodigoEmpresa.text.toString()
            val email = view.txtCorreoEmpleado.text.toString()
            val password = view.txtPasswordEmpleado.text.toString()
            val confirmpassword = view.txtConfirmPasswordEmplead.text.toString()
            if (isValid(name, codigo, email, password, confirmpassword)) {
                if (isValidEmail(email)) {
                    if (isValidConfirmPassword(password, confirmpassword)) {
                        var nDialog = ProgressDialog(this!!.activity) //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
                        nDialog.setMessage("Loading..")
                        nDialog.setTitle("Registrando")
                        nDialog.isIndeterminate = false
                        nDialog.setCancelable(true)
                        nDialog.show()
                        val usuario = Usuario()
                        usuario.name = name
                        usuario.id_empresa = codigo
                        usuario.email = email
                        usuario.ubicacion = imag
                        usuario.rol = "usuario"
                        usuario.direccion = ""
                        usuario.id = ""
                        usuario.edad
                        usuario.telefono = ""
                        //first save the user on authentication firebase, after that save the user on cloud firestore
                        validCode(email, password, usuario, view)
                        Handler().postDelayed({ nDialog.dismiss() }, 1000)
                    } else {
                        view.txtConfirmPasswordEmplead.error = "Las contraseñas no coinciden"
                    }
                } else {
                    view.txtCorreoEmpleado.error = "Ingresa un correo valido"
                }
            } else {
                Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
            }
        }//end for listner
        view.txtCorreoEmpleado.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                view.txtCorreoEmpleado.error = if (isValidEmail(view.txtCorreoEmpleado.text.toString())) null else "El email no es valido"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        view.txtConfirmPasswordEmplead.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                view.txtConfirmPasswordEmplead.error = if (isValidConfirmPassword(view.txtPasswordEmpleado.text.toString(), view.txtConfirmPasswordEmplead.text.toString())) null else "Las cotraseñas no coinciden"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        view.btnLogin2.setOnClickListener {
            val intent = Intent(context, LoginCardOverlap::class.java)
            startActivityForResult(intent, 0)
        }
    }

    /**
     * @param email
     * @param password
     * @param usuario
     * in this handler the register on authentication with firebase
     */
    private fun signUpByEmail(email: String, password: String, usuario: Usuario, view: View) {
        //get instance of firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                //save the user on cloud firestore
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(requireActivity()) {
                    //save the user on cloud firestore
                    saveUsuario(usuario, view)
                    status=true
                    val intent = Intent(context, LoginCardOverlap::class.java)
                    startActivityForResult(intent, 0)
                    mAuth.signOut()//this is necesary because the val is in general
                    Toast.makeText(context, "Se ha enviado un correo de confirmacion", Toast.LENGTH_LONG).show()
                }
            } else {
                // toast("Los Datos ingresados ya estan registrados,intenta con uno nuevo")
                Toast.makeText(context, "Los Datos ingresados ya estan registrados,intenta con uno nuevo", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * @param usuario
     * in this handler the save user on cloud firestore on the collection with name Empresa
     */
    private fun saveUsuario(usuario: Usuario, view: View) {
        //the first case is Valid if the id_empresa already exists
        usuariosCollection.add(usuario).addOnSuccessListener {
            usuariosCollection.document(it.id).update("id", it.id).addOnSuccessListener {
            }.addOnFailureListener { }
        }.addOnFailureListener {
            Toast.makeText(context, "Error guardando el usuario, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }
//handler

    private fun validCode(email: String, password: String, usuario: Usuario, view: View) {
        val resultado = empresasCollection.whereEqualTo("id_empresa", usuario.id_empresa)
        //beggin with consult
        resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var con = 0
                for (document in task.result!!) {
                    con++
                }
                if (con == 0) {
                    status=true
                    view.txtCodigoEmpresa.error = "Codigo incorrecto"
                } else {
                    signUpByEmail(email, password, usuario, view)
                }
            } else {
                Log.w("EXCEPTION", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
    }

    /**
     * @param name
     * @param giro @param email @param telefono @param password @param confirmpassword
     */
    private fun isValid(name: String, codigo: String, email: String, password: String, confirmpassword: String): Boolean {
        return !name.isNullOrEmpty() &&
                !codigo.isNullOrEmpty() &&
                !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                !confirmpassword.isNullOrEmpty()
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        // Necesita Contener -->    1 Num / 1 Minuscula / 1 Mayuscula / 1 Special / Min Caracteres 4
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }

    fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    fun EditText.validate(validation: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                validation(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}