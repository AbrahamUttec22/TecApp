package com.material.tecgurus.register
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.tecgurus.R
import com.material.tecgurus.activity.login.LoginCardOverlap
import com.material.tecgurus.model.Empresa
import kotlinx.android.synthetic.main.tab1_fragment.view.*
import kotlinx.android.synthetic.main.tab1_fragment.view.txtNombreEmpresa
import kotlinx.android.synthetic.main.tab1_fragment.view.txtTelefonoEmpresa
import java.util.regex.Pattern
import kotlin.random.Random
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.message.ApiClient
import com.material.tecgurus.message.ApiInter
import com.material.tecgurus.message.Notification
import com.material.tecgurus.message.RequestNotificaton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham
 * Usuario
 */
class Tab1Fragment : Fragment(), View.OnClickListener {

    override fun onClick(position: View?) {
    }

    private val TAG = "Tab1Fragment"
    private var btnTEST: Button? = null
    //declare val for save the collection
    private val marksCollection: CollectionReference
    private val adminCollection: CollectionReference

    //declare val for save the collection
    private val usuariosCollection: CollectionReference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Empresas")
        usuariosCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        adminCollection = FirebaseFirestore.getInstance().collection("Administrador")
        mAuth.signOut()
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
                        var nDialog = ProgressDialog(this!!.activity) //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
                        nDialog.setMessage("Loading..")
                        nDialog.setTitle("Registrando")
                        nDialog.setIndeterminate(false)
                        nDialog.setCancelable(true)
                        nDialog.show()
                        val empresa = Empresa()
                        val c = Calendar.getInstance()
                        val df = SimpleDateFormat("dd/MM/yyyy")
                        val formattedDate = df.format(c.getTime()).toString()

                        val calendar = Calendar.getInstance()
                        calendar.time = Date()
                        var calendarTime = Calendar.DAY_OF_MONTH//obtener el dia del mes
                        var temp = calendar.get(calendarTime)// obetnemos el dia
                        calendar.set(calendarTime, temp + 15)
                        val dfVencimiento = SimpleDateFormat("dd/MM/yyyy")
                        val fecha_vencimiento = dfVencimiento.format(calendar.getTime()).toString()

                        empresa.fecha_registro = formattedDate
                        empresa.nombre = name
                        empresa.correo = email
                        empresa.telefono = telefono
                        empresa.giro = giro
                        empresa.direccion = ""
                        empresa.foto = imag
                        empresa.fecha_vencimiento_plan=fecha_vencimiento
                        empresa.id_empresa = ""
                        empresa.estatus="pruebainicial"

                        //first save the user on authentication firebase, after that save the user on cloud firestore
                        signUpByEmail(email, password, empresa)
                        Handler().postDelayed({ nDialog.dismiss() }, 1000)
                    } else {
                        view.txtConfirmPasswordEmpresa.error = "Las contraseñas no coinciden"
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
        val randomValues = Random.nextInt(1000, 9000)
        var nuevo = ""
        if (empresa.nombre.contentEquals(" ") || empresa.nombre.contains(" ")) {
            nuevo = empresa.nombre.split(" ")[1].trim()
        } else {
            nuevo = empresa.nombre.trim()
        }

        empresa.uid = ""
        empresa.id_empresa = nuevo + randomValues.toString()//save the company
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(requireActivity()) {

                }
                //save the user on cloud firestore
                saveEmpresa(empresa)
                mAuth.signOut()//this is necesary because the val is in general
                showConfirmDialog()
            } else {
                Toast.makeText(context, "Los Datos ingresados ya estan registrados,intenta con uno nuevo", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showConfirmDialog() {
        //the header from dialog
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_send_email)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
            dialog.dismiss()
            val intent = Intent(context, LoginCardOverlap::class.java)
            startActivityForResult(intent, 0)
        }
        dialog.show()
        dialog.window!!.attributes = lp
        dialog.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(p0: DialogInterface?) {
                val intent = Intent(context, LoginCardOverlap::class.java)
                startActivityForResult(intent, 0)
            }

        })

    }

    /**
     * @param empresa
     * in this handler the save user on cloud firestore on the collection with name Empresa
     */
    private fun saveEmpresa(empresa: Empresa) {
        empresa.token = ""
        //add the collection and save the User, this is validated
        marksCollection.add(empresa).addOnSuccessListener {
            sendNotificationToPatner(empresa.nombre)
        }.addOnFailureListener {
            Toast.makeText(context, "Error guardando la empresa, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * @param nombre
     */
    private fun sendNotificationToPatner(nombre: String) {
        val consul = adminCollection
        //beggin with consult
        try {
            consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    var token = ""
                    for (document in task.result!!) {
                        token = document.get("token").toString()
                        val notification = Notification(nombre + " se ha registrado a Hola! Comunicate", "Empresas")
                        val requestNotificaton = RequestNotificaton()
                        //token is id , whom you want to send notification ,
                        requestNotificaton.token = token
                        requestNotificaton.notification = notification
                        val apiService = ApiClient.getClient().create(ApiInter::class.java)
                        val responseBodyCall = apiService.sendChatNotification(requestNotificaton)
                        responseBodyCall.enqueue(object : Callback<ResponseBody> {
                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
                        })
                    }
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })//end for expression lambdas this very cool

        } catch (e: Exception) {

        }

    }

    /**
     * @param name
     * @param giro
     * @param email
     * @param telefono
     * @param password
     * @param confirmpassword
     */
    private fun isValid(name: String, giro: String, email: String, telefono: String, password: String, confirmpassword: String): Boolean {
        return !name.isNullOrEmpty() &&
                !giro.isNullOrEmpty() &&
                !telefono.isNullOrEmpty() &&
                !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                !confirmpassword.isNullOrEmpty()
    }

    /**
     * @param email
     */
    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    /**
     * @param password
     */
    fun isValidPassword(password: String): Boolean {
        // Necesita Contener -->    1 Num / 1 Minuscula / 1 Mayuscula / 1 Special / Min Caracteres 4
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPattern)
        return pattern.matcher(password).matches()
    }

    /**
     * @param password
     * @param confirmPassword
     */
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