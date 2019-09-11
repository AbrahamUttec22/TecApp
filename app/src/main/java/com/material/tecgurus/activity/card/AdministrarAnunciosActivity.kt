package com.material.tecgurus.activity.card

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.R
import com.material.tecgurus.adapter.AdministrarAnuncioAdapter
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.model.Anuncio
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_administrar_anuncios.*
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
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        anunciosCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
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
        val consul = anunciosCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemAnuncio = ArrayList<Anuncio>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                    itemAnuncio.add(document.toObject(Anuncio::class.java))//ir agregando los datos a la lista
                }
                if (con == 0) {
                    iconDefaultAdminAnuncios.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultAdminAnuncios.setVisibility(View.INVISIBLE)
                }
                eventoList = itemAnuncio
                adapter = AdministrarAnuncioAdapter(this, R.layout.list_view_administrar_anuncio, eventoList)
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
        val itemAnuncio = ArrayList<Anuncio>()//lista local de una sola instancia
        var con = 0
        for (change in changes) {
            con++
            itemAnuncio.add(change.document.toObject(Anuncio::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        if (con == 0) {
            iconDefaultAdminAnuncios.setVisibility(View.VISIBLE)
        } else {
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
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Administrar Anuncios"
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
