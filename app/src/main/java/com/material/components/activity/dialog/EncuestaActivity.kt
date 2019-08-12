package com.material.components.activity.dialog

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.EncuestaAdapter
import com.material.components.model.Encuesta
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.list_view_encuesta.*
import kotlinx.android.synthetic.main.list_view_encuesta.view.*
import java.util.ArrayList
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.inflate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.activity.MainMenu
import com.material.components.adapter.AdministrarEventoAdapter
import com.material.components.adapter.EncuestaViewHolder
import com.material.components.drawer.DashboarActivity
import com.material.components.model.Evento
import kotlinx.android.synthetic.main.activity_administrar_evento.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_encuesta.listView


/**
 * @author Abraham
 * see the encuests
 */
class EncuestaActivity : AppCompatActivity() {

    private lateinit var adapter: EncuestaAdapter
    private lateinit var encuestaList: List<Encuesta>
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private val channelId = "com.example.vicky.notificationexample"
    //declare val for save the collection
    private val encuestaCollection: CollectionReference
    private val votacionCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        encuestaCollection = FirebaseFirestore.getInstance().collection("Encuestas")
        votacionCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encuesta)
        initToolbar()
        addMarksListenerVoto()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeVerEncuestas)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false);
        })
        //end for click listener a second boton
    }


    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa = sharedPreference.getString("id_empresa", "")
        encuestaCollection.whereEqualTo("status", "1").whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
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

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListenerVoto() {
        votacionCollection.addSnapshotListener { snapshots, error ->
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
        val consul = encuestaCollection.whereEqualTo("status", "1").whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemEncuesta = ArrayList<Encuesta>()//lista local de una sola instancia
                var con = 0
                for (document in task.result!!) {
                    con++
                    itemEncuesta.add(document.toObject(Encuesta::class.java))//ir agregando los datos a la lista
                }
                if (con == 0) {
                    iconDefaultEncuestas.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultEncuestas.setVisibility(View.INVISIBLE)
                }
                adapter = EncuestaAdapter(this, R.layout.list_view_encuesta, itemEncuesta)
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
        val itemUsuario = ArrayList<Encuesta>()//lista local de una sola instancia
        var con = 0
        for (change in changes) {
            con++
            itemUsuario.add(change.document.toObject(Encuesta::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la
        if (con == 0) {
            iconDefaultEncuestas.setVisibility(View.VISIBLE)
        } else {
            iconDefaultEncuestas.setVisibility(View.INVISIBLE)
        }
        adapter = EncuestaAdapter(this, R.layout.list_view_encuesta, itemUsuario)
        listView.adapter = adapter
    }

    //front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Encuestas"
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
