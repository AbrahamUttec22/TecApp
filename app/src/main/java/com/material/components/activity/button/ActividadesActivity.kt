package com.material.components.activity.button

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.goToActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.activity.dialog.EncuestaActivity
import com.material.components.adapter.ActividadesAdapter
import com.material.components.adapter.EncuestaAdapter
import com.material.components.adapter.EventoAdapter
import com.material.components.drawer.DashboarActivity
import com.material.components.model.Actividades
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_actividades.*
import kotlinx.android.synthetic.main.activity_dashboard_grid_fab.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_encuesta.listView
import java.util.ArrayList

/**
 * @author Abraham Casas Aguilar
 */
class ActividadesActivity : AppCompatActivity() {

    private lateinit var adapter: ActividadesAdapter
    //declare val for save the collection
    private val actividadesCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    // private val channelId = "com.material.components.activity"
    private val channelId = "com.example.vicky.notificationexample"
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividades)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeActividades)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false)
        })
        //end for click listener a second boton
    }

    private fun notifi() {
        val mBuilder: NotificationCompat.Builder
        val mNotifyMgr = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val icono = R.mipmap.ic_launcher
        val i = Intent(this, ActividadesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, i, 0)
        mBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("Actividades")
                .setContentText("Se te asigno una nueva actividad")
                .setVibrate(longArrayOf(100, 250, 100, 500))
                .setAutoCancel(true)
        mNotifyMgr.notify(1, mBuilder.build())
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        //var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        // var id_empresa = sharedPreference.getString("id_empresa", "")
        var email = mAuth.currentUser!!.email.toString()
        actividadesCollection.whereEqualTo("correo", email).addSnapshotListener { snapshots, error ->
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
        var email = mAuth.currentUser!!.email.toString()
        val consul = actividadesCollection.whereEqualTo("correo", email)
        //beggin with consult
        consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                val itemActividad = ArrayList<Actividades>()//lista local de una sola instanciavar
                var con = 0
                for (document in task.result!!) {
                    con++
                    val actividad = Actividades()
                    actividad.fecha_hora_asignada = document.get("fecha_hora_asignada").toString()
                    actividad.fecha_hora_terminada = document.get("fecha_hora_terminada").toString()
                    actividad.correo = document.get("correo").toString()
                    actividad.id_usuario = document.get("id_usuario").toString()
                    actividad.id = document.get("id").toString()
                    actividad.estatus = document.get("estatus").toString()
                    actividad.actividad = document.get("actividad").toString()
                    itemActividad.add(actividad)
                }
                if (con == 0) {
                    iconDefaultActividad.setVisibility(View.VISIBLE)
                } else {
                    iconDefaultActividad.setVisibility(View.INVISIBLE)
                }

                adapter = ActividadesAdapter(this, R.layout.list_view_actividades, itemActividad)
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
        val itemActividad = ArrayList<Actividades>()//lista local de una sola instanciavar
        var con = 0
        for (change in changes) {
            con++
            itemActividad.add(change.document.toObject(Actividades::class.java))//ir agregando los datos a la lista
            // notifi()
        }//una ves agregado los campos mandar a llamar la vista
        if (con == 0) {
            iconDefaultActividad.setVisibility(View.VISIBLE)
        } else {
            iconDefaultActividad.setVisibility(View.INVISIBLE)
        }

        adapter = ActividadesAdapter(this, R.layout.list_view_actividades, itemActividad)
        listView.adapter = adapter
    }

    //front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Actividades"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
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