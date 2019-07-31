package com.material.components.activity.card

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
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
import com.material.components.adapter.AdministrarAnuncioAdapter
import com.material.components.adapter.AdministrarEventoAdapter
import com.material.components.model.Anuncio
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_administrar_anuncios.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_encuesta.listView
import java.util.ArrayList

/**
 * @author Abraham Casas Aguilar
 * admin anuncios
 */
class AdministrarAnunciosActivity : AppCompatActivity() {

    private lateinit var adapter: AdministrarAnuncioAdapter
    private lateinit var eventoList: List<Anuncio>
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    //declare val for save the collection
    private val anunciosCollection: CollectionReference
    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        anunciosCollection = FirebaseFirestore.getInstance().collection("Anuncios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrar_anuncios)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeAdministrarAnuncios)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false);
        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences ("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa=sharedPreference.getString ("id_empresa","")
        anunciosCollection.whereEqualTo("id_empresa",id_empresa).addSnapshotListener { snapshots, error ->
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
        val itemAnuncio = ArrayList<Anuncio>()//lista local de una sola instancia
        var con=0
        for (change in changes) {
            con++
            itemAnuncio.add(change.document.toObject(Anuncio::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        if(con==0){
            iconDefaultAdminAnuncios.setVisibility(View.VISIBLE)
        }else{
            iconDefaultAdminAnuncios.setVisibility(View.INVISIBLE)
        }
        eventoList = itemAnuncio
        adapter = AdministrarAnuncioAdapter(this, R.layout.list_view_administrar_anuncio, eventoList)
        //listView.btnCerrarEncuesta
        listView.adapter = adapter
    }//end for handler

    //front end only
    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Administrar")
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
