package com.material.components.register

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.model.Empresa
import kotlinx.android.synthetic.main.tab1_fragment.view.*
import kotlinx.android.synthetic.main.tab1_fragment.view.txtNombreEmpresa
import kotlinx.android.synthetic.main.tab1_fragment.view.txtTelefonoEmpresa
import java.util.regex.Pattern
import kotlin.random.Random
import android.content.Intent
import com.alejandrolora.finalapp.isValidEmail
import com.material.components.activity.MainMenu

/**
 * @author Abraham
 * Company
 */
class Tab1Fragment : Fragment(), View.OnClickListener {

    override fun onClick(position: View?) {

    }

    private val TAG = "Tab1Fragment"
    private var btnTEST: Button? = null

    //declare val for save the collection
    private val marksCollection: CollectionReference
    //declare val for save the collection
    private val usuariosCollection: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Empresas")
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.tab1_fragment, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //the image for default
        val imag = "https://firebasestorage.googleapis.com/v0/b/tecapp-25ed3.appspot.com/o/usuarios%2Fic_default_user.png?alt=media&token=7e64e478-9802-4149-915a-dba6d3c69c59"//this is a image for default
        //on listener for the buton on register a new user
        view.btnRegistrarEmpresa.setOnClickListener {
            //direction foto and id_empresa is empty first
            val name = view.txtNombreEmpresa.text.toString()
            val giro = view.txtTelefonoEmpresa.text.toString()
            val email = view.txtCorreoEmpresa.text.toString()
            val telefono = view.txtTelefonoEmpresa.text.toString()
            val password = view.txtPasswordEmpresa.text.toString()
            val confirmpassword = view.txtConfirmPasswordEmpresa.text.toString()
            if (isValid(name, giro, email, telefono, password, confirmpassword)) {
                if (isValidEmail(email)) {
                    if (isValidConfirmPassword(password, confirmpassword)) {
                        val empresa = Empresa()
                        empresa.nombre = name
                        empresa.correo = email
                        empresa.telefono = telefono
                        empresa.giro = giro
                        empresa.direccion = ""
                        empresa.foto = imag
                        empresa.id_empresa = ""
                        //first save the user on authentication firebase, after that save the user on cloud firestore
                        signUpByEmail(email, password, empresa)
                    } else {
                        view.txtConfirmPasswordEmpresa.error = "Ingresa un correo valido"
                    }
                } else {
                    view.txtCorreoEmpresa.error = "Ingresa un correo valido"
                }
            } else {
                Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
            }
        }//end for listner

        view.txtCorreoEmpresa.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                view.txtCorreoEmpresa.error = if (isValidEmail(view.txtCorreoEmpresa.text.toString())) null else "El email no es valido"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        view.txtConfirmPasswordEmpresa.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                view.txtConfirmPasswordEmpresa.error = if (isValidConfirmPassword(view.txtPasswordEmpresa.text.toString(), view.txtConfirmPasswordEmpresa.text.toString())) null else "Las cotraseñas no coinciden"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        view.btnLogin1.setOnClickListener {
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
    private fun signUpByEmail(email: String, password: String, empresa: Empresa) {
        //get instance of firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                //save the user on cloud firestore
                mAuth.signOut()//this is necesary because the val is in general
                saveEmpresa(empresa)
            } else {
                // toast("Los Datos ingresados ya estan registrados,intenta con uno nuevo")
                Toast.makeText(context, "Error guardando la empresa, intenta de nuevo", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * @param usuario
     * in this handler the save user on cloud firestore on the collection with name Empresa
     */
    private fun saveEmpresa(empresa: Empresa) {
        //add the collection and save the User, this is validated
        val randomValues = List(1) { Random.nextInt(1000, 20000) }
        empresa.id_empresa = empresa.nombre + randomValues//save the company
        marksCollection.document(empresa.id_empresa).set(empresa).addOnSuccessListener {
            val sharedPreference =this.activity!!.getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
            var sesion = sharedPreference.edit()
            sesion.putString("id_empresa", empresa.id_empresa)
            sesion.commit()
            val intent = Intent(context, MainMenu::class.java)
            startActivityForResult(intent, 0)
        }.addOnFailureListener {
            Toast.makeText(context, "Error guardando el usuario, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * @param name
     * @param giro @param email @param telefono @param password @param confirmpassword
     */
    private fun isValid(name: String, giro: String, email: String, telefono: String, password: String, confirmpassword: String): Boolean {
        return !name.isNullOrEmpty() &&
                !giro.isNullOrEmpty() &&
                !telefono.isNullOrEmpty() &&
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