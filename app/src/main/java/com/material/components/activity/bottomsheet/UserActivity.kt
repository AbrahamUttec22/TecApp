package com.material.components.activity.bottomsheet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

class UserActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var personList: List<Usuario>

    //declare val for save the collection
    private val userCollection: CollectionReference

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
    }

    private fun getPersons(): List<Usuario> {
        return listOf(
                Usuario("Alejandro", 5, "27", "Primero"),
                Usuario("Fernando", 5, "27", "Primero"),
                Usuario("Alicia", 5, "27", "Primero"),
                Usuario("Paula", 5, "27", "Primero"),
                Usuario("Alberto", 5, "27", "Primero"),
                Usuario("Cristian", 5, "27", "Primero"),
                Usuario("Octavio", 5, "27", "Primero"),
                Usuario("Yaiza", 5, "27", "Primero"),
                Usuario("Naomi", 5, "27", "Primero")
        )
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        userCollection.addSnapshotListener { snapshots, error ->
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
    }


    /**
     * initToolbar(header)
     */
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.title = "Usuarios de TecApp"
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
        } else if (item.itemId == R.id.action_settings) {//icono de mas opciones para agregar usuario
            /*val intento1 = Intent(this, FormSignUp::class.java)
            startActivity(intento1)*/
            startActivity(Intent(this, FormSignUp::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}
