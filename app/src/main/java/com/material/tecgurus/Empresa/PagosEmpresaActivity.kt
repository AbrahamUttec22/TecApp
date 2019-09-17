package com.material.tecgurus.Empresa

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
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.R
import com.material.tecgurus.adapter.PagosAllAdapter
import com.material.tecgurus.adapter.PagosEmpresaAdapter
import com.material.tecgurus.admin.AdminDashboardActivity
import com.material.tecgurus.drawer.DashboarActivity
import com.material.tecgurus.model.Pagos
import com.material.tecgurus.utils.Tools
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_pagos_all.*
import kotlinx.android.synthetic.main.activity_pagos_all.listViewPagos
import kotlinx.android.synthetic.main.activity_pagos_empresa.*
import java.util.ArrayList

/**
 * Created by:
 * @author Abraham Casas Aguilar
 */
class PagosEmpresaActivity : AppCompatActivity() {

    private lateinit var adapter: PagosEmpresaAdapter
    //declare val for save the collection
    private val pagosCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var pagosList: List<Pagos>

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        pagosCollection = FirebaseFirestore.getInstance().collection("Pagos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagos_empresa)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipePagos)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false)
        })
    }

    /**
     * Listener for empresaCollection
     */
    private fun addMarksListener() {
        //addSnapshotListener es para saber si ha ocurrido un cambio en la base de datos
        pagosCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    listenerDb()
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listenerDb() {
        //beggin with consult
        var sharedPreferencet = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreferencet.getString("id_empresa", "")
        val consulta = pagosCollection.whereEqualTo("id_empresa", id_empresa)
        consulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemPagos = ArrayList<Pagos>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                    itemPagos.add(document.toObject(Pagos::class.java))//ir agregando los datos a la lista
                }
                if (con == 0) {
                    iconDefaultPagosEmpresa.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultPagosEmpresa.setVisibility(View.INVISIBLE)
                }
                pagosList = itemPagos
                adapter = PagosEmpresaAdapter(this, R.layout.list_view_empresas_pagos, pagosList)
                listViewPagos.adapter = adapter

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool
    }

    /**
     * initToolbar(header)
     */
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Pagos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)//menu de las opciones del toolbar, menu basic
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            goToActivity<DashboarActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        } else if (item.itemId == R.id.action_search) {//icono de search
            //Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
            toast("Diste click en search")
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
