package com.material.components.activity.form

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.model.Anuncio
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_agregar_anuncio.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author  Abraham
 * 21/06/2019
 */
class AgregarAnuncioActivity : AppCompatActivity() {


    //declare val for save the collection
    private val marksCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Anuncios")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_anuncio)
        initToolbar()
        registrarAnuncio.setOnClickListener {
            val description=txtDescriptionAnuncio.text.toString()
            val titulo=txtTituloAnuncio.text.toString()
            if (isValid(description,titulo)){
                val obj=Anuncio()
                obj.description=description
                obj.titulo=titulo
                val c = Calendar.getInstance()
                val df = SimpleDateFormat("dd/MM/yyyy")
                val formattedDate = df.format(c.getTime()).toString()
                obj.fecha=formattedDate
                saveAnuncio(obj)
            }else{
                toast("Completa los campos")
            }
        }
    }

    //back
    //backend
    /**
     * @param anuncio
     * in this handler the save anuncio on cloud firestore on the collection with name Anuncio
     */
    private fun saveAnuncio(anuncio: Anuncio) {
        //add the collection and save the User, this is validated
        marksCollection.add(anuncio).addOnSuccessListener {
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
