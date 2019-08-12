package com.material.components.activity.form

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.material.components.R
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_form_profile_data.*
import java.util.*
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.widget.*
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.model.Evento
import java.text.SimpleDateFormat
import android.provider.MediaStore
import android.content.Context
import android.util.Log
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.material.components.activity.MainMenu
import com.material.components.drawer.DashboarActivity
import java.io.IOException

import com.material.components.message.ApiClient
import com.material.components.message.ApiInter
import com.material.components.message.Notification
import com.material.components.message.RequestNotificaton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author  Abraham
 * add a new event
 */
class FormProfileData : AppCompatActivity() {

    var calendario = Calendar.getInstance()
    private var array_states: Array<String>? = null
    private var mStorageRef: StorageReference? = null
    private val PICK_PHOTO = 1
    lateinit var uri: Uri
    //declare val for save the collection
    private val marksCollection: CollectionReference
    private val userCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        mStorageRef = FirebaseStorage.getInstance().getReference()
        marksCollection = FirebaseFirestore.getInstance().collection("Eventos")
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_profile_data)
        array_states = resources.getStringArray(R.array.states)
        initToolbar()
        etBirthday.setOnClickListener {
            DatePickerDialog(this, date, calendario
                    .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)).show()
        }

        registrarEvento.setOnClickListener {
            val description = txtDescription.text.toString()
            val fecha = etBirthday.text.toString()
            val titulo = txtTitulo.text.toString()
            if (isValid(description, fecha, titulo) && btnGaleriaEvento.getDrawable() != null) {
                //agregar linea del valor de la imagen
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = "Registrando..."
                builder.setView(dialogView)
                val evento = Evento()
                val sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                sharedPreference.getString("id_empresa", "")
                evento.id_empresa = sharedPreference.getString("id_empresa", "").toString()
                evento.description = description
                evento.fecha = fecha
                evento.titulo = titulo
                evento.id = ""
                upload(evento)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                Handler().postDelayed({ dialog.dismiss() }, 1500)
            } else {
                toast("Completa los campos")
            }
        }// end for listener

        btnImageEvento.setOnClickListener {
            abrirFotoGaleria()
        }//end for listener imagen evento
    }

    //backend

    private fun upload(evento: Evento) {
        //gs://tecapp-25ed3.appspot.com/uploads/image:40366
        var mReference = mStorageRef!!.child("eventos/" + uri.lastPathSegment)
        var uploadTask = mReference.putFile(uri)
        try {
            uploadTask.addOnProgressListener {

            }.addOnPausedListener {

            }.addOnSuccessListener {

            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                mReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl: Uri? = task.result
                    evento.ubicacion = downloadUrl.toString()
                    saveEvento(evento)
                } else {

                }
            }.addOnFailureListener { e ->

            }
        } catch (e: Exception) {
            toast("" + e.toString())
        }
    }

    /**
     * @param evento
     * in this handler the save evento on cloud firestore on the collection with name Eventos
     */
    private fun saveEvento(evento: Evento) {
        //add the collection and save the User, this is validated
        marksCollection.add(evento).addOnSuccessListener {
            marksCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                val empleado = userCollection.whereEqualTo("id_empresa", evento.id_empresa)
                //beggin with consult
                empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            val token = document.get("token").toString()
                            sendNotificationToPatner(token)
                        }
                    } else {
                        Log.w("saasas", "Error getting documents.", task.exception)
                    }
                })//end for expression lambdas this very cool

            }.addOnFailureListener { }
            Toast.makeText(this, "Evento registrado con exito", Toast.LENGTH_LONG).show()
            onBackPressed()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el evento, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendNotificationToPatner(token: String) {
        val notification = Notification("Se ha agregado un nuevo evento", "Eventos")
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

    private fun abrirFotoGaleria() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val filePath = data!!.getData()
            try {
                val bitmapImagen = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                btnGaleriaEvento.setImageBitmap(bitmapImagen)
                uri = data!!.data
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //see views front end
    private var date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        calendario.set(Calendar.YEAR, year)
        calendario.set(Calendar.MONTH, monthOfYear)
        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        actualizarInput()
    }

    private fun actualizarInput() {
        val formatoDeFecha = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(formatoDeFecha, Locale.US)
        etBirthday.setText(sdf.format(calendario.time))
    }

    private fun initToolbar() {

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Agregar Evento"
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

    private fun isValid(description: String, fecha: String, titulo: String): Boolean {
        return !description.isNullOrEmpty() &&
                !fecha.isNullOrEmpty() &&
                !titulo.isNullOrEmpty()
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
