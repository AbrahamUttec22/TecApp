package com.material.tecgurus.admin

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
import com.material.tecgurus.adapter.CostosAdapter
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.model.Anuncio
import com.material.tecgurus.model.Costos
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_administrar_anuncios.*
import kotlinx.android.synthetic.main.activity_administrar_anuncios.listView
import kotlinx.android.synthetic.main.activity_costos.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import java.util.ArrayList

/**
 * Created by:
 * @author Abraham Casas Aguilar
 * Planes mensualmente
 */
class CostosActivity : AppCompatActivity() {

    private lateinit var adapter: CostosAdapter
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    //declare val for save the collection
    private val costosCollection: CollectionReference
    private lateinit var costoList: List<Costos>

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        costosCollection = FirebaseFirestore.getInstance().collection("Costos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_costos)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeCostosMensuales)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false);
        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        costosCollection.whereEqualTo("tipo_plan", "mensual").addSnapshotListener { snapshots, error ->
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

        val consul = costosCollection.whereEqualTo("tipo_plan", "mensual")
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemCostos = ArrayList<Costos>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                    itemCostos.add(document.toObject(Costos::class.java))//ir agregando los datos a la lista
                }

                costoList = itemCostos
                adapter = CostosAdapter(this, R.layout.list_view_costos_mensuales, costoList)
                //listView.btnCerrarEncuesta
                listViewCostosMensuales.adapter = adapter
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }

        })//end for expression lambdas this very cool


    }

    //front end only
    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Costos Mensuales"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            goToActivity<AdminDashboardActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        goToActivity<AdminDashboardActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }


}
