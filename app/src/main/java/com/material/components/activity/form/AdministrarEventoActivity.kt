package com.material.components.activity.form

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.AdministrarEventoAdapter
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_encuesta.*
import java.util.ArrayList
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.adapter.EventoAdapter
import com.material.components.drawer.DashboarActivity
import kotlinx.android.synthetic.main.activity_administrar_evento.*
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_encuesta.listView


/**
 * @author Abraham
 * this admin for the events
 */
class AdministrarEventoActivity : AppCompatActivity() {

    private lateinit var adapter: AdministrarEventoAdapter
    private lateinit var eventoList: List<Evento>
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

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
        swipeRefreshLayout = findViewById(R.id.swipeEvento)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false);

        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        eventosCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    //addChanges(changes)
                listenerDb()
                }
            } else {
                toast("Ha ocurrido un error intente de nuevo")
            }
        }
    }

    private fun listenerDb() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        val consul = eventosCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemEvento = ArrayList<Evento>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                    val evento = Evento()
                    evento.description = document.get("description").toString()
                    evento.fecha = document.get("fecha").toString()
                    evento.id = document.get("id").toString()
                    evento.id_empresa = document.get("id_empresa").toString()
                    evento.image = document.get("image").toString()
                    evento.titulo = document.get("titulo").toString()
                    evento.ubicacion = document.get("ubicacion").toString()
                    evento.hora = document.get("hora").toString()
                    itemEvento.add(evento)
                }
                if (con == 0) {
                    iconDefaultAdminEventos.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultAdminEventos.setVisibility(View.INVISIBLE)
                }
                eventoList = itemEvento
                adapter = AdministrarEventoAdapter(this, R.layout.list_view_administrar_eveto, itemEvento)
                //listView.btnCerrarEncuesta
                listView.adapter = adapter
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool


    }


    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemEvento = ArrayList<Evento>()//lista local de una sola instancia
        var con = 0
        for (change in changes) {
            con++
            itemEvento.add(change.document.toObject(Evento::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        if (con == 0) {
            iconDefaultAdminEventos.setVisibility(View.VISIBLE)
        } else {
            iconDefaultAdminEventos.setVisibility(View.INVISIBLE)
        }
        eventoList = itemEvento
        adapter = AdministrarEventoAdapter(this, R.layout.list_view_administrar_eveto, itemEvento)
        //listView.btnCerrarEncuesta
        listView.adapter = adapter
    }//end for handler


    //front end only
    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Administrar Eventos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

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