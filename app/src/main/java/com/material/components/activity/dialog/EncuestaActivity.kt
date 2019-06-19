package com.material.components.activity.dialog

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.EncuestaAdapter
import com.material.components.adapter.EventoAdapter
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_encuesta.listView
import java.util.ArrayList

/**
 * @author Abraham
 */
class EncuestaActivity : AppCompatActivity() {

    private lateinit var adapter: EncuestaAdapter
    private lateinit var encuestaList: List<Encuesta>

    //declare val for save the collection
    private val userCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Encuestas")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuesta)
        initToolbar()
        addMarksListener()
       // encuestaList = getPersons()


    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        userCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes)
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemUsuario = ArrayList<Encuesta>()//lista local de una sola instancia
        for (change in changes) {
            itemUsuario.add(change.document.toObject(Encuesta::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        adapter = EncuestaAdapter(this, R.layout.list_view_encuesta, itemUsuario)
        listView.adapter = adapter
    }


    private fun getPersons(): List<Encuesta> {
        return listOf(
                Encuesta("Alejandro", "Lora"),
                Encuesta("Fernando", "Vega"),
                Encuesta("Alicia", "Gómez"),
                Encuesta("Paula", "Escobar"),
                Encuesta("Alberto", "Parada"),
                Encuesta("Cristian", "Romero"),
                Encuesta("Octavio", "Hernández"),
                Encuesta("Yaiza", "Costi"),
                Encuesta("Naomi", "Fernandexz")
        )
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Encuestas")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
