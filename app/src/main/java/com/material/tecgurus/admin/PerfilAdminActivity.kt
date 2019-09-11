package com.material.tecgurus.admin

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
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_perfil_admin.*

/**
 * @author Abraham Casas Aguilar
 */
class PerfilAdminActivity : AppCompatActivity() {

    //get instance of firebase
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val adminCollection: CollectionReference
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
        adminCollection = FirebaseFirestore.getInstance().collection("Administrador")
        mStorageRef = FirebaseStorage.getInstance().getReference()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sendDta()
        setContentView(R.layout.activity_perfil_admin)
        initToolbar()
        imgPerfilAdministrador.setOnClickListener {
            abrirFotoGaleria()
        }
    }

    /**
     * send information to view Mi perfil
     */
    private fun sendDta() {
        if (mAuth.currentUser != null) {
            var email = mAuth.currentUser!!.email.toString()
            val resultado = adminCollection.whereEqualTo("correo", email)
            //beggin with consult
            resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val email = document.get("correo").toString()
                        view = findViewById<View>(R.id.imgPerfilAdministrador) as ImageView
                        Glide
                                .with(applicationContext)
                                .load(document.get("imagen").toString())
                                .into(view)
                        //nombre
                        txtNombreAdmin.setText("Administrador")
                        //email
                        txtEmailAdmin.setText(email)
                        //direccion
                        // txtDireccionEmpresa.setText(direccion)
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
            val resultado = adminCollection.whereEqualTo("correo", email)
            //beggin with consult
            resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        var imagen = document.get("imagen").toString()
                        view = findViewById<View>(R.id.imgPerfilAdministrador) as ImageView
                        Glide
                                .with(applicationContext)
                                .load(imagen)
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
    private fun isValid(nombre: String, giro: String, telefono: String, direccion: String): Boolean {
        return !nombre.isNullOrEmpty() &&
                !giro.isNullOrEmpty() && !telefono.isNullOrEmpty() && !direccion.isNullOrEmpty()
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
                uri = data!!.data
                val builder = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                val message = dialogView.findViewById<TextView>(R.id.mensaje)
                message.text = "Subiendo..."
                builder.setView(dialogView)
                builder.setCancelable(false)
                dialog = builder.create()
                upload()
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
            uploadTask.addOnProgressListener {
            }.continueWithTask { task ->
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
                Log.w("PERFILEMP", e.toString())
            }
        } catch (e: Exception) {
            Log.w("PERFILEMP", e.toString())
        }
    }

    private fun updateImage(imagen: String) {
        adminCollection.document(idDocument).update("imagen", imagen).addOnSuccessListener {
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
            goToActivity<AdminDashboardActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        goToActivity<AdminDashboardActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


}
