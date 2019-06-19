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
import java.text.SimpleDateFormat


/**
 * @author  Abraham
 * add a new event
 */
class FormProfileData : AppCompatActivity() {


    var calendario = Calendar.getInstance()
    private var array_states: Array<String>? = null

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
}
