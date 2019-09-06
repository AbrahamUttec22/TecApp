package com.material.components.activity.form

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.material.components.R
import com.material.components.drawer.DashboarActivity
import com.material.components.model.Anuncio
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_agregar_anuncio.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

import com.material.components.message.ApiClient
import com.material.components.message.ApiInter
import com.material.components.message.Notification
import com.material.components.message.RequestNotificaton
import com.material.components.model.Encuesta
import com.material.components.model.detalleAnuncios
import com.material.components.model.detalleEncuestas
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author  Abraham
 * 21/06/2019
 */
class AgregarAnuncioActivity : AppCompatActivity() {

    //declare val for save the collection
    private val marksCollection: CollectionReference
    private var mStorageRef: StorageReference? = null
    private val PICK_PHOTO = 1
    private val userCollection: CollectionReference
    private val empresaCollection: CollectionReference
    private val detalleAnunciosCollection: CollectionReference
    lateinit var uri: Uri

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        mStorageRef = FirebaseStorage.getInstance().getReference()
        marksCollection = FirebaseFirestore.getInstance().collection("Anuncios")
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        detalleAnunciosCollection = FirebaseFirestore.getInstance().collection("detalleAnuncios")
    }

    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_anuncio)
        initToolbar()
        registrarAnuncio.setOnClickListener {
            val description = txtDescriptionAnuncio.text.toString()
            val titulo = txtTituloAnuncio.text.toString()
            if (isValid(description, titulo) && imgAnuncio.getDrawable() != null) {
                try {
                    val builder = AlertDialog.Builder(this)
                    val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                    val message = dialogView.findViewById<TextView>(R.id.mensaje)
                    message.text = "Registrando..."
                    val obj = Anuncio()
                    var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
                    var id_empresa = sharedPreference.getString("id_empresa", "")
                    obj.id_empresa = id_empresa
                    obj.description = description
                    obj.titulo = titulo
                    val c = Calendar.getInstance()
                    val df = SimpleDateFormat("dd/MM/yyyy")
                    val formattedDate = df.format(c.getTime()).toString()
                    obj.fecha = formattedDate
                    upload(obj)
                    builder.setView(dialogView)
                    builder.setCancelable(false)
                    dialog = builder.create()
                    dialog.show()
                    // Handler().postDelayed({ dialog.dismiss() }, 1600)
                } catch (e: java.lang.Exception) {
                    toast("" + e)
                }

            } else {
                toast("Completa los campos")
            }
        }
        btnImageE.setOnClickListener {
            abrirFotoGaleria()
        }
    }

    //back
    private fun upload(anuncio: Anuncio) {
        //gs://tecapp-25ed3.appspot.com/uploads/image:40366
        var mReference = mStorageRef!!.child("anuncios/" + uri.lastPathSegment)
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
                    anuncio.ubicacion = downloadUrl.toString()
                    saveAnuncio(anuncio)
                } else {

                }
            }.addOnFailureListener { e ->

            }
        } catch (e: Exception) {
            toast("" + e.toString())
        }
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
                imgAnuncio.setImageBitmap(bitmapImagen)
                uri = data.data
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //backend
    /**
     * @param anuncio
     * in this handler the save anuncio on cloud firestore on the collection with name Anuncio
     */
    private fun saveAnuncio(anuncio: Anuncio) {
        //add the collection and save the User, this is validated
        marksCollection.add(anuncio).addOnSuccessListener {
            anuncio.id = it.id
            marksCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                val empleado = userCollection.whereEqualTo("id_empresa", anuncio.id_empresa)
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
                        saveDetalleAnucio(anuncio)
                    } else {
                        Log.w("saasas", "Error getting documents.", task.exception)
                    }
                })//end for expression lambdas this very cool

            }.addOnFailureListener { }
            dialog.dismiss()
            toast("Anuncio registrado con exito")
            deregreso()
        }.addOnFailureListener {
            toast("Error guardando el evento, intenta de nuevo")
        }
    }

    private fun sendNotificationToPatner(token: String) {
        val notification = Notification("Se ha agregado un nuevo anuncio", "Anuncios")
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
     * @param anuncio
     */
    private fun saveDetalleAnucio(anuncio: Anuncio) {
        //First I found all users for the company with that id_empresa
        val consultaUsuarios = userCollection.whereEqualTo("id_empresa", anuncio.id_empresa)
        //beggin with consult
        consultaUsuarios.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    //well, now I save on detalleEvento
                    var detalle = detalleAnuncios()
                    detalle.correo_usuario = document.get("email").toString()
                    detalle.id_empresa = anuncio.id_empresa
                    detalle.estatus = "0"
                    detalle.id_anuncio = anuncio.id
                    detalle.id = ""
                    detalleAnunciosCollection.add(detalle).addOnSuccessListener {
                        detalleAnunciosCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                        }.addOnFailureListener { }
                    }.addOnFailureListener {
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

        val consultaEmpresa = empresaCollection.whereEqualTo("id_empresa", anuncio.id_empresa)
        //beggin with consult
        consultaEmpresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    //well, now I save on detalleEvento
                    var detalle = detalleAnuncios()
                    detalle.correo_usuario = document.get("correo").toString()
                    detalle.id_empresa = anuncio.id_empresa
                    detalle.estatus = "0"
                    detalle.id_anuncio = anuncio.id
                    detalle.id=""
                    detalleAnunciosCollection.add(detalle).addOnSuccessListener {
                        detalleAnunciosCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                        }.addOnFailureListener { }
                    }.addOnFailureListener {
                    }
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool

    }


    //see views not back
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Agregar Anuncio"
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

    private fun isValid(description: String, titulo: String): Boolean {
        return !description.isNullOrEmpty() &&
                !titulo.isNullOrEmpty()
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