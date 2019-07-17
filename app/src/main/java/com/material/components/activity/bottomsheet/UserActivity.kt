package com.material.components.activity.bottomsheet
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
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.activity.form.FormSignUp
import com.material.components.adapter.UserAdapter
import com.material.components.model.Usuario
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_user.*
import java.util.ArrayList

/**
 * @author Abraham
 * admin for empleados
 */
class UserActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    //declare val for save the collection
    private val userCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeUsuario)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false)

        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences ("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa=sharedPreference.getString ("id_empresa","")
        userCollection.whereEqualTo("id_empresa",id_empresa).addSnapshotListener { snapshots, error ->
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
        val itemUsuario = ArrayList<Usuario>()//lista local de una sola instancia
        for (change in changes) {
            itemUsuario.add(change.document.toObject(Usuario::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        adapter = UserAdapter(this, R.layout.list_view_usuario, itemUsuario)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            if(i==0)
            toast("click")
            else
                toast("not clicked")
        }
    }

    /**
     * initToolbar(header)
     */
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = "Usuarios"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_basic, menu)//menu de las opciones del toolbar, menu basic
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_search) {//icono de search
            //Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
            toast("Diste click en search")
        }
        return super.onOptionsItemSelected(item)
    }
}