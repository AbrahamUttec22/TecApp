package com.material.components.activity.card

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
import android.widget.AdapterView
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.adapter.EventoAdapter
import com.material.components.adapter.UserAdapter
import com.material.components.drawer.DashboarActivity
import com.material.components.model.Evento
import com.material.components.utils.Tools
import java.util.*

import com.material.components.message.ApiClient
import com.material.components.message.ApiInter
import com.material.components.message.Notification
import com.material.components.message.RequestNotificaton
import com.material.components.model.Usuario
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_card_basic.listView
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.list_view_usuario.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author abraham
 * This class was in java but now is in kotlin
 * 19/06/2019 9:39 am
 * See events
 */
class CardBasic : AppCompatActivity() {

    private lateinit var adapter: EventoAdapter
    private lateinit var personList: List<Evento>
    //declare val for save the collection
    private val eventoCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    // private val channelId = "com.material.components"
    private val channelId = "com.example.vicky.notificationexample"

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        eventoCollection = FirebaseFirestore.getInstance().collection("Eventos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_basic)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeEventoUsuario)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false)
        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        eventoCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    //addChanges(changes)
                    listenerDb()
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listenerDb() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        val consul = eventoCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemUsuario = ArrayList<Evento>()//lista local de una sola instancia
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
                    itemUsuario.add(evento)
                }
                if (con == 0) {
                    iconDefaultEvento.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultEvento.setVisibility(View.INVISIBLE)
                }
                adapter = EventoAdapter(this, R.layout.list_view_evento, itemUsuario)
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
        val itemUsuario = ArrayList<Evento>()//lista local de una sola instancia
        var con = 0
        for (change in changes) {
            con++
            itemUsuario.add(change.document.toObject(Evento::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la
        if (con == 0) {
            iconDefaultEvento.setVisibility(View.VISIBLE)
        } else {
            iconDefaultEvento.setVisibility(View.INVISIBLE)
        }
        adapter = EventoAdapter(this, R.layout.list_view_evento, itemUsuario)
        listView.adapter = adapter
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Eventos"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
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
