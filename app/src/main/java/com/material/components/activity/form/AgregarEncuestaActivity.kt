package com.material.components.activity.form

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.material.components.R
import com.material.components.adapter.EncuestaAdapter
import com.material.components.model.Encuesta
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_form_profile_data.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author abraham
 * add a new encuesta
 */
class AgregarEncuestaActivity : AppCompatActivity() {
    var calendario = Calendar.getInstance()
    private var array_states: Array<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_encuesta)
        array_states = resources.getStringArray(R.array.states)
        initToolbar()
        etBirthday.setOnClickListener {
            DatePickerDialog(this, date, calendario
                    .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

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
