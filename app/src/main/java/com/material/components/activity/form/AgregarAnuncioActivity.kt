package com.material.components.activity.form

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.material.components.R
import com.material.components.model.Anuncio
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_agregar_anuncio.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author  Abraham
 * 21/06/2019
 */
class AgregarAnuncioActivity : AppCompatActivity() {

    //declare val for save the collection
    private val marksCollection: CollectionReference
    private var mStorageRef: StorageReference? = null
    private val PICK_PHOTO = 1
    lateinit var uri: Uri

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        mStorageRef = FirebaseStorage.getInstance().getReference()
        marksCollection = FirebaseFirestore.getInstance().collection("Anuncios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_anuncio)
        initToolbar()
        registrarAnuncio.setOnClickListener {
            val description=txtDescriptionAnuncio.text.toString()
            val titulo=txtTituloAnuncio.text.toString()
            if (isValid(description,titulo) && imgAnuncio.getDrawable() != null){
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = "Registrando..."
                val obj=Anuncio()
                var sharedPreference = getSharedPreferences ("shared_login_data", Context.MODE_PRIVATE)
                var id_empresa=sharedPreference.getString ("id_empresa","")
                obj.id_empresa=id_empresa
                obj.description=description
                obj.titulo=titulo
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("dd/MM/yyyy")
                val formattedDate = df.format(c.getTime()).toString()
                obj.fecha=formattedDate
                upload(obj)
                builder.setView(dialogView)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
                Handler().postDelayed({ dialog.dismiss() }, 1600)
            }else{
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
            uploadTask.addOnProgressListener { taskSnapshot ->

            }.addOnPausedListener {

            }.addOnSuccessListener { taskSnapshot ->

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
                uri = data!!.data
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
            marksCollection.document(it.id).update("id",it.id).addOnSuccessListener {}.addOnFailureListener { }
            toast("Anuncio registrado con exito")
            onBackPressed()
        }.addOnFailureListener {
            toast("Error guardando el evento, intenta de nuevo")
        }
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
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValid(description:String,titulo:String):Boolean{
     return !description.isNullOrEmpty() &&
             !titulo.isNullOrEmpty()
    }

}
