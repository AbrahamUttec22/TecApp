package com.material.components.activity.form

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.alejandrolora.finalapp.toast
import com.material.components.R
import com.material.components.adapter.EncuestaAdapter
import com.material.components.model.Encuesta
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_agregar_encuesta.*
import kotlinx.android.synthetic.main.activity_form_profile_data.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.material.components.model.Evento

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

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        marksCollection = FirebaseFirestore.getInstance().collection("Encuestas")
    }

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
                            var sharedPreference = getSharedPreferences ("shared_login_data", Context.MODE_PRIVATE)
                            var id_empresa=sharedPreference.getString ("id_empresa","")
                            encuesta.id_empresa=id_empresa
                            encuesta.pregunta = pregunta
                            encuesta.status = "1"
                            encuesta.respuestas = listOf(txtRespuestas.text.toString(), txtRespuestas2.text.toString())

                            saveEncuesta(encuesta)
                            builder.setCancelable(false)
                            val dialog = builder.create()
                            dialog.show()
                            Handler().postDelayed({ dialog.dismiss() }, 1500)
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
                            var sharedPreference = getSharedPreferences ("shared_login_data", Context.MODE_PRIVATE)
                            var id_empresa=sharedPreference.getString ("id_empresa","")
                            encuesta.id_empresa=id_empresa
                            encuesta.pregunta = pregunta
                            encuesta.status = "1"
                            encuesta.respuestas = listOf(txtRespuestas.text.toString(), txtRespuestas2.text.toString(), txtRespuestas3.text.toString())
                            saveEncuesta(encuesta)
                            builder.setCancelable(false)
                            val dialog = builder.create()
                            dialog.show()
                            Handler().postDelayed({ dialog.dismiss() }, 1500)
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

    private var date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        // TODO Auto-generated method stub
        calendario.set(Calendar.YEAR, year)
        calendario.set(Calendar.MONTH, monthOfYear)
        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        actualizarInput()
    }

    //backend
    private fun isValid(pregunta: String): Boolean {
        return !pregunta.isNullOrEmpty()
    }

    private fun saveEncuesta(encuesta: Encuesta) {
        //add the collection and save the User, this is validated
        marksCollection.document(encuesta.pregunta).set(encuesta).addOnSuccessListener {
            toast("Encuesta registrado con exito")
            onBackPressed()
        }.addOnFailureListener {
            toast("Error guardando la encuesta, intenta de nuevo")
        }
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
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
