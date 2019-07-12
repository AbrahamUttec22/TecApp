package com.material.components.activity.form

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.AdministrarEventoAdapter
import com.material.components.adapter.EstadisticaAdapter
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.list_view_estadistica.view.*
import java.util.ArrayList

/**
 * @author Abraham
 * this admin for the events
 */
class AdministrarEventoActivity : AppCompatActivity() {

    private lateinit var adapter: AdministrarEventoAdapter
    private lateinit var eventoList: List<Evento>

    //declare val for save the collection
    private val eventosCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        eventosCollection = FirebaseFirestore.getInstance().collection("Eventos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrar_evento)
        initToolbar()
        addMarksListener()
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        eventosCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes)
                }
            } else {
                toast("Ha ocurrido un error intente de nuevo")
            }
        }
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemEvento = ArrayList<Evento>()//lista local de una sola instancia
        for (change in changes) {
            itemEvento.add(change.document.toObject(Evento::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        eventoList = itemEvento
        adapter = AdministrarEventoAdapter(this, R.layout.list_view_administrar_eveto, eventoList)
        //listView.btnCerrarEncuesta
        listView.adapter = adapter
    }//end for handler

    //front end only
    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Administrar Eventos")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}