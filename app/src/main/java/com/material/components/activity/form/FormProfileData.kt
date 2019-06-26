package com.material.components.activity.form

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.material.components.R
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_form_profile_data.*
import android.widget.EditText
import java.util.*
import android.widget.DatePicker
import android.app.DatePickerDialog
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.model.Evento
import java.text.SimpleDateFormat


/**
 * @author  Abraham
 * add a new event
 */
class FormProfileData : AppCompatActivity() {


    var calendario = Calendar.getInstance()
    private var array_states: Array<String>? = null

    //declare val for save the collection
    private val marksCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Eventos")
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
            if (isValid(description, fecha, titulo)) {
                val evento = Evento()
                // evento.image =
                evento.description = description
                evento.fecha = fecha
                evento.titulo = titulo
                saveEvento(evento)
            }else{
                toast("Completa los campos")
            }
        }// end for listener
    }

    //backend
    /**
     * @param evento
     * in this handler the save evento on cloud firestore on the collection with name Eventos
     */
    private fun saveEvento(evento: Evento) {
        //add the collection and save the User, this is validated
        marksCollection.add(evento).addOnSuccessListener {
            Toast.makeText(this, "Evento registrado con exito", Toast.LENGTH_LONG).show()
            onBackPressed()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el evento, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    //see views
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
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValid(description: String, fecha: String, titulo: String): Boolean {
        return !description.isNullOrEmpty() &&
                !fecha.isNullOrEmpty() &&
                !titulo.isNullOrEmpty()
    }
}
