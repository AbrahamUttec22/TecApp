package com.material.tecgurus.activity.card

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.material.tecgurus.R
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_card_overlaps.*

/**
 * @author  Abraham
 * 28/06/2019
 * My perfil
 */
class CardOverlap : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userCollection: CollectionReference
    private var idDocument: String = ""
    private var view: ImageView? = null
    private var mStorageRef: StorageReference? = null
    private val PICK_PHOTO = 1
    lateinit var uri: Uri
    private var viewTwo: ImageView? = null
    lateinit var dialog: AlertDialog

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        mStorageRef = FirebaseStorage.getInstance().getReference()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendDta()
        setContentView(R.layout.activity_card_overlaps)
        initToolbar()
        imgPerfil.setOnClickListener {
            abrirFotoGaleria()
        }
        btnUpdateInformation.setOnClickListener {
            val nombre = txtNombre.text.toString()
            val edad = txtEdad.text.toString()
            val direccion = txtDireccion.text.toString()
            val telefono = txtTelefono.text.toString()
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
            val message = dialogView.findViewById<TextView>(R.id.mensaje)
            message.text = "Actualizando"
            builder.setView(dialogView)
            builder.setCancelable(false)
            updateInformation(nombre, edad, direccion, telefono)
            dialog = builder.create()
            dialog.show()
            //Handler().postDelayed({ dialog.dismiss() }, 1000)
        }
    }

    /**
     * send information to view Mi perfil
     */
    private fun sendDta() {
        if (mAuth.currentUser != null) {
            var email = mAuth.currentUser!!.email.toString()
            val resultado = userCollection.whereEqualTo("email", email)
            //beggin with consult
            resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val nombre = document.get("name").toString()
                        val edad = document.get("edad").toString()
                        val telefono = document.get("telefono").toString()
                        val email = document.get("email").toString()
                        val direccion = document.get("direccion").toString()
                        view = findViewById<View>(R.id.imgPerfil) as ImageView
                        Glide
                                .with(applicationContext)
                                .load(document.get("ubicacion").toString())
                                .into(view)
                        //nombre
                        txtNombre.setText(nombre)
                        //edad
                        txtEdad.setText(edad)
                        //telefono
                        txtTelefono.setText(telefono)
                        //email
                        txtEmail.setText(email)
                        //direccion
                        txtDireccion.setText(direccion)
                        idDocument = document.getId()
                    }
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })
        }
    }

    private fun seeImagePerfil() {
        if (mAuth.currentUser != null) {
            var email = mAuth.currentUser!!.email.toString()
            val resultado = userCollection.whereEqualTo("email", email)
            //beggin with consult
            resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val ubicacion = document.get("ubicacion").toString()
                        view = findViewById<View>(R.id.imgPerfil) as ImageView
                        Glide
                                .with(applicationContext)
                                .load(ubicacion)
                                .into(view)
                    }
                }
            })
            dialog.dismiss()
        }
    }

    /**
     * @param direccion
     */
    private fun isValid(nombre: String, edad: String, direccion: String, telefono: String): Boolean {
        return !nombre.isNullOrEmpty() &&
                !edad.isNullOrEmpty() && !direccion.isNullOrEmpty() && !telefono.isNullOrEmpty()
    }

    private fun updateInformation(name: String, edad: String, direccion: String, telefono: String) {
        userCollection.document(idDocument).update("name", name, "edad", edad, "direccion", direccion, "telefono", telefono).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(this, "Informacion actulizada", Toast.LENGTH_LONG).show()
            onBackPressed()
            //  onBackPressed()
        }.addOnFailureListener {
            dialog.dismiss()
            Toast.makeText(this, "Error actualizando la informacion, intenta de nuevo", Toast.LENGTH_LONG).show()
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
            if (requestCode == PICK_PHOTO) {
                uri = data!!.data!!
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = "Subiendo..."
                builder.setView(dialogView)
                builder.setCancelable(false)
                upload()
                dialog = builder.create()
                dialog.show()

                //Handler().postDelayed({ dialog.dismiss() }, 1400)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload() {
        var mReference = mStorageRef!!.child("usuarios/" + uri.lastPathSegment)
        var uploadTask = mReference.putFile(uri)
        try {
            uploadTask.addOnProgressListener {}.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                mReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl: Uri? = task.result
                    updateImage(downloadUrl.toString())
                    seeImagePerfil()

                } else {

                }
            }.addOnFailureListener { e ->
                Log.w("CARDOVER", e.toString())

            }
        } catch (e: Exception) {
            Log.w("CARDOVER", e.toString())
        }
    }

    private fun updateImage(imagen: String) {
        userCollection.document(idDocument).update("ubicacion", imagen).addOnSuccessListener {
            //  Toast.makeText(this, "Informacion actulizada", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error actualizando la informacion, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    //unique views
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Mi perfil"
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
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        goToActivity<DashboarActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}