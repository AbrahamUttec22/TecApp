package com.material.tecgurus.activity.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.material.tecgurus.R
import com.material.tecgurus.utils.Tools
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.widget.TextView
import com.alejandrolora.finalapp.toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.UploadTask
import com.material.tecgurus.model.ImagenProyecto

/**
 * @author Abraham
 */
class StorageActivity : AppCompatActivity() {

    private val PICK_PHOTO = 1
    private var mStorageRef: StorageReference? = null
    //get instance of firebase
    // private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var uri: Uri
    private val registrosCollection: CollectionReference

    init {
        FirebaseApp.initializeApp(this)
        mStorageRef = FirebaseStorage.getInstance().getReference()
        registrosCollection = FirebaseFirestore.getInstance().collection("Imagenes")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage2)
        initToolbar()
    }

    //backend
    private fun abrirFotoGaleria() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), PICK_PHOTO)
    }

    //intent for the image in activity storage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO) {
                uri = data!!.data!!
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = "Subiendo..."
                builder.setView(dialogView)
                builder.setCancelable(false)
                upload()
                val dialog = builder.create()
                dialog.show()
                Handler().postDelayed({ dialog.dismiss() }, 1000)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload() {
        //gs://tecapp-25ed3.appspot.com/uploads/image:40366
        var mReference = mStorageRef!!.child("uploads/" + uri.lastPathSegment)
        try {
            mReference.putFile(uri).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                var imagen = ImagenProyecto()
                imagen.name = uri.lastPathSegment.toString()
                imagen.ubicacion = mReference.toString()
                saveAnuncio(imagen)
            }
        } catch (e: Exception) {
            toast("" + e.toString())
        }
    }

    /**
     * @param anuncio
     * in this handler the save anuncio on cloud firestore on the collection with name Anuncio
     */
    private fun saveAnuncio(imagen: ImagenProyecto) {
        //add the collection and save the User, this is validated
        registrosCollection.add(imagen).addOnSuccessListener {
        }.addOnFailureListener {
            toast("Error guardando la imagen, intenta de nuevo")
        }
    }

    //front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Imagenes")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_imagenes, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        } else {
            abrirFotoGaleria()
        }
        return super.onOptionsItemSelected(item)
    }

}