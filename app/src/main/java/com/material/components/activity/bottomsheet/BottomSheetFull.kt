package com.material.components.activity.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

import com.material.components.R
import com.material.components.activity.form.FormSignUp
import com.material.components.adapter.AdapterPeopleLeft
import com.material.components.data.DataGenerator
import com.material.components.fragment.FragmentBottomSheetDialogFull
import com.material.components.model.People
import com.material.components.model.Usuario
import com.material.components.utils.Tools
import java.util.*

/**
 * @author Abraham
 *  * This class was in java but now is in kotlin
 * 18/06/2019 9:39 am
 * See users
 */
class BottomSheetFull : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var adapter: AdapterPeopleLeft? = null
    //declare val for save the collection
    private val peopleCollection: CollectionReference


    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        peopleCollection = FirebaseFirestore.getInstance().collection("Usuarios")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet_full)
        initComponent()
        initToolbar()
    }

    private fun initComponent() {
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        addMarksListener()//base de datos de cloud firestore

        // display first sheet
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        peopleCollection.addSnapshotListener { snapshots, error ->
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
        addToList(itemUsuario)//vista
    }

    /**
     * @param itemUsuario
     */
    private fun addToList(itemUsuario: List<Usuario>) {
        val itemPeople = ArrayList<People>()//lista de people
        val drw_arr = this.getResources().obtainTypedArray(R.array.people_images)//contexto de imagenes
        for (item in itemUsuario) {//recorremos la lista de usuario para agregarlo a la lista de people
            var obj = People()//instancia de la clase People
            //Almacenar las variables de peopleCollection a la de itemPeople
           // obj.image = drw_arr.getResourceId(item.image, -1)
            obj.imageDrw = this.getResources().getDrawable(obj.image)
            obj.name = item.name
            obj.email = item.email
            itemPeople.add(obj)//agregando
        }
        //terminado el for mandamos a llamar el adapterpeopleft
        itemPeople.shuffle()
        adapter = AdapterPeopleLeft(this, itemPeople)//mandar el conjunto de datos
        recyclerView!!.adapter = adapter

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
        } else if (item.itemId== R.id.action_search) {//icono de search
            //Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
            toast("Diste click en search")
        }else if  (item.itemId== R.id.action_settings) {//icono de mas opciones para agregar usuario
            /*val intento1 = Intent(this, FormSignUp::class.java)
            startActivity(intento1)*/
            startActivity(Intent(this, FormSignUp::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}
