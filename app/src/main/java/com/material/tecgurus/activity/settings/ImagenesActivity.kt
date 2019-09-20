package com.material.tecgurus.activity.settings

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.material.tecgurus.R
import com.material.tecgurus.adapter.ImagenAdapter
import com.material.tecgurus.model.ImagenProyecto
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_imagenes.*


/**
 * @author Abraham
 * 28/06/2019
 */
class ImagenesActivity : AppCompatActivity() {

    //this a simple configuration for the view Adapter
    private lateinit var adapter: ImagenAdapter
    private lateinit var personImagen: List<ImagenProyecto>
    private var imagenes = arrayOf("2131231079")
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
        setContentView(R.layout.activity_imagenes)
        initToolbar()
        //Log.w("IMAGENES",""+imagenes.toList())
        //personImagen=getImagen()
        addMarksListener()
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        registrosCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes)
                }
            } else {
                toast("Ha ocurrido un error intenta de nuevo")
            }
        }
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemUsuario = ArrayList<ImagenProyecto>()//lista local de una sola instancia
        for (change in changes) {
            itemUsuario.add(change.document.toObject(ImagenProyecto::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        adapter = ImagenAdapter(this, R.layout.list_view_imagen, itemUsuario)
        listView.adapter = adapter//

    }

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
                    var imagen = ImagenProyecto()
                    imagen.name = uri.lastPathSegment.toString()
                    imagen.ubicacion = downloadUrl.toString()
                    saveAnuncio(imagen)
                } else {

                }
            }.addOnFailureListener { e ->
                Log.w("IMAGENESAC", e.toString())
            }
        } catch (e: Exception) {
            Log.w("IMAGENESAC", e.toString())
        }
    }

    /**
     * @param anuncio
     * in this handler the save anuncio on cloud firestore on the collection with name Anuncio
     */
    private fun saveAnuncio(imagen: ImagenProyecto) {
        //add the collection and save the User, this is validated
        registrosCollection.add(imagen).addOnSuccessListener {
            addMarksListener()
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
