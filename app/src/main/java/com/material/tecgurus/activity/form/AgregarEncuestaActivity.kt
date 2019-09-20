package com.material.tecgurus.activity.form

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.alejandrolora.finalapp.toast
import com.material.tecgurus.R
import com.material.tecgurus.model.Encuesta
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_agregar_encuesta.*
import kotlinx.android.synthetic.main.activity_form_profile_data.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.drawer.DashboarActivity

import com.material.tecgurus.message.ApiClient
import com.material.tecgurus.message.ApiInter
import com.material.tecgurus.message.Notification
import com.material.tecgurus.message.RequestNotificaton
import com.material.tecgurus.model.detalleEncuestas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

/**
 * @author abraham
 * add a new encuesta
 */
class AgregarEncuestaActivity : AppCompatActivity() {

    var calendario = Calendar.getInstance()
    private val marksCollection: CollectionReference
    private var array_states: Array<String>? = null
    private var spiner: Spinner? = null
    private var valorRespuestas = 0
    private val userCollection: CollectionReference
    private val empresaCollection: CollectionReference
    private val detalleEncuestasCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Encuestas")
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        detalleEncuestasCollection = FirebaseFirestore.getInstance().collection("detalleEncuestas")
    }

    lateinit var dialog: AlertDialog

    //in this parshat the source is long because i Need validations
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_encuesta)
        array_states = resources.getStringArray(R.array.states)//ge the array for spiner
        val adapter = ArrayAdapter.createFromResource(this, R.array.respuestas_array, android.R.layout.simple_spinner_item)
        spiner = findViewById(R.id.Respuestas)//find the spiner
        spiner!!.adapter = adapter//inflate the Adapter with the array og the strings.xml
        initToolbar()
        //Listener for spinner while the user clicked on the how many answer
        spiner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //with the objective the show the editText for answers
                if (position == 1) {
                    txtRespuestas.setVisibility(View.VISIBLE)
                    txtRespuestas2.setVisibility(View.VISIBLE)
                    txtRespuestas3.setVisibility(View.INVISIBLE)
                    valorRespuestas = position
                } else if (position == 2) {
                    txtRespuestas.setVisibility(View.VISIBLE)
                    txtRespuestas2.setVisibility(View.VISIBLE)
                    txtRespuestas3.setVisibility(View.VISIBLE)
                    valorRespuestas = position
                } else if (position == 0) {
                    txtRespuestas.setVisibility(View.INVISIBLE)
                    txtRespuestas2.setVisibility(View.INVISIBLE)
                    txtRespuestas3.setVisibility(View.INVISIBLE)
                    valorRespuestas = 0
                }
            }
        }

        btnRegistrarEncuesta.setOnClickListener {
            val pregunta = txtPregunta.text.toString()
            if (isValid(pregunta) && valorRespuestas > 0) {
                //this switch for know which val I save on Cloud firestore
                when (valorRespuestas) {
                    1 -> {
                        if (!txtRespuestas.text.isNullOrEmpty() && !txtRespuestas2.text.isNullOrEmpty()) {
                            val builder = AlertDialog.Builder(this)
                            val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                            val message = dialogView.findViewById<TextView>(R.id.mensaje)
                            message.text = "Registrando..."
                            builder.setView(dialogView)
                            var encuesta = Encuesta()//this isn´t global because the instance
                            var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                            var id_empresa = sharedPreference.getString("id_empresa", "")
                            encuesta.id_empresa = id_empresa.toString()
                            encuesta.pregunta = pregunta
                            encuesta.status = "1"
                            encuesta.respuestas = listOf(txtRespuestas.text.toString(), txtRespuestas2.text.toString())

                            saveEncuesta(encuesta)
                            builder.setCancelable(false)
                            dialog = builder.create()
                            dialog.show()
                            // Handler().postDelayed({ dialog.dismiss() }, 1500)
                        } else
                            toast("Completa los campos")
                    }
                    2 -> {
                        if (!txtRespuestas.text.isNullOrEmpty() && !txtRespuestas2.text.isNullOrEmpty() && !txtRespuestas3.text.isNullOrEmpty()) {
                            val builder = AlertDialog.Builder(this)
                            val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                            val message = dialogView.findViewById<TextView>(R.id.mensaje)
                            message.text = "Registrando..."
                            builder.setView(dialogView)
                            var encuesta = Encuesta()//this isn´t global because the instance
                            var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                            var id_empresa = sharedPreference.getString("id_empresa", "")
                            encuesta.id_empresa = id_empresa.toString()
                            encuesta.pregunta = pregunta
                            encuesta.status = "1"
                            encuesta.respuestas = listOf(txtRespuestas.text.toString(), txtRespuestas2.text.toString(), txtRespuestas3.text.toString())
                            saveEncuesta(encuesta)
                            builder.setCancelable(false)
                            dialog = builder.create()
                            dialog.show()
                            //Handler().postDelayed({ dialog.dismiss() }, 1500)
                        } else
                            toast("Completa los campos")
                    }
                    else -> {
                    }//default
                }
            } else {
                toast("Completa los campos ")
            }
        }
    }

    //backend
    private fun isValid(pregunta: String): Boolean {
        return !pregunta.isNullOrEmpty()
    }

    private fun saveEncuesta(encuesta: Encuesta) {
        //add the collection and save the User, this is validated
        marksCollection.document(encuesta.pregunta).set(encuesta).addOnSuccessListener {
            val empleado = userCollection.whereEqualTo("id_empresa", encuesta.id_empresa)
            //beggin with consult
            empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val token = document.get("token").toString()
                        sendNotificationToPatner(token)
                        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                        var toke = sharedPreference.getString("token", "").toString()
                        if (toke.equals(token)) {

                        } else {
                            sendNotificationToPatner(token)
                        }
                    }
                    saveDetalleEncuesta(encuesta)
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })//end for expression lambdas this very cool
            dialog.dismiss()
            toast("Encuesta registrado con exito")
            deregreso()
        }.addOnFailureListener {
            toast("Error guardando la encuesta, intenta de nuevo")
        }
    }

    private fun sendNotificationToPatner(token: String) {
        val notification = Notification("Se ha agregado una nueva encuesta", "Encuestas")
        val requestNotificaton = RequestNotificaton()
        //token is id , whom you want to send notification ,
        requestNotificaton.token = token
        requestNotificaton.notification = notification
        val apiService = ApiClient.getClient().create(ApiInter::class.java!!)
        val responseBodyCall = apiService.sendChatNotification(requestNotificaton)
        responseBodyCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    /**
     * @param encuesta
     */
    private fun saveDetalleEncuesta(encuesta: Encuesta) {
        //First I found all users for the company with that id_empresa
        val consultaUsuarios = userCollection.whereEqualTo("id_empresa", encuesta.id_empresa)
        //beggin with consult
        consultaUsuarios.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    //well, now I save on detalleEvento
                    var detalle = detalleEncuestas()
                    detalle.correo_usuario = document.get("email").toString()
                    detalle.id_empresa = encuesta.id_empresa
                    detalle.estatus = "0"
                    detalle.id_encuesta = encuesta.pregunta
                    detalle.id=""
                    detalleEncuestasCollection.add(detalle).addOnSuccessListener {
                        detalleEncuestasCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                        }.addOnFailureListener { }
                    }.addOnFailureListener {
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        val consultaEmpresa = empresaCollection.whereEqualTo("id_empresa", encuesta.id_empresa)
        //beggin with consult
        consultaEmpresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    //well, now I save on detalleEvento
                    var detalle = detalleEncuestas()
                    detalle.correo_usuario = document.get("correo").toString()
                    detalle.id_empresa = encuesta.id_empresa
                    detalle.estatus = "0"
                    detalle.id_encuesta = encuesta.pregunta
                    detalleEncuestasCollection.add(detalle).addOnSuccessListener {
                        detalleEncuestasCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                        }.addOnFailureListener { }
                    }.addOnFailureListener {
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }


    //front end
    private fun actualizarInput() {
        val formatoDeFecha = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(formatoDeFecha, Locale.US)
        etBirthday.setText(sdf.format(calendario.time))
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Agregar Encuesta"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        deregreso()
    }

    private fun deregreso() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}