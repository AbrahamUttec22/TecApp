package com.material.components.activity.card

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.adapter.EventoAdapter
import com.material.components.adapter.UserAdapter
import com.material.components.model.Evento
import com.material.components.model.Usuario
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_card_basic.listView
import kotlinx.android.synthetic.main.activity_user.*
import java.util.*

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
    private val userCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
   // private val channelId = "com.material.components"
    private val channelId = "com.example.vicky.notificationexample"
    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Eventos")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_basic)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeEventoUsuario)
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
        userCollection.whereEqualTo("id_empresa", id_empresa).addSnapshotListener { snapshots, error ->
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

    private fun notifi() {
        val mBuilder: NotificationCompat.Builder
        val mNotifyMgr = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val icono = R.mipmap.ic_launcher
        val i = Intent(this, CardBasic::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, i, 0)
        mBuilder = NotificationCompat.Builder(applicationContext, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("Eventos")
                .setContentText("Se ha agregado un nuevo evento")
                .setVibrate(longArrayOf(100, 250, 100, 500))
                .setAutoCancel(true)
        mNotifyMgr.notify(1, mBuilder.build())
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemUsuario = ArrayList<Evento>()//lista local de una sola instancia
        for (change in changes) {
            itemUsuario.add(change.document.toObject(Evento::class.java))//ir agregando los datos a la lista
            notifi()
        }//una ves agregado los campos mandar a llamar la vista
        adapter = EventoAdapter(this, R.layout.list_view_evento, itemUsuario)
        listView.adapter = adapter
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Eventos")
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
