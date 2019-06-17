package com.material.components.activity.bottomsheet

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
import com.material.components.adapter.AdapterPeopleLeft
import com.material.components.data.DataGenerator
import com.material.components.fragment.FragmentBottomSheetDialogFull
import com.material.components.model.People
import com.material.components.model.Usuario
import com.material.components.utils.Tools
import java.util.*

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
        //Toast.makeText(this, "Swipe up bottom sheet", Toast.LENGTH_SHORT).show();
    }

    private fun initComponent() {
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        addMarksListener()//base de datos de cloud firestore
        //set data and list adapter
        //val itemPeople = ArrayList<People>()


        // display first sheet
        /*   FragmentBottomSheetDialogFull fragment = new FragmentBottomSheetDialogFull();
        fragment.setPeople(adapter.getItem(0));
        fragment.show(getSupportFragmentManager(), fragment.getTag());*/
    }


    private fun addMarksListener() {
        peopleCollection.addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    addChanges(changes)
                }
            } else {
                Toast.makeText(this, "Ha ocurrido un error leyendo las notas", Toast.LENGTH_SHORT).show()
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
            itemUsuario.add(change.document.toObject(Usuario::class.java))//ir agregando los datos
        }//una ves agregado los campos mandar a llamar la vista
        addToList(itemUsuario)
    }

    /**
     * @param itemUsuario
     */
    private fun addToList(itemUsuario: List<Usuario>) {
        val itemPeople = ArrayList<People>()//lista de people
        val drw_arr = this.getResources().obtainTypedArray(R.array.people_images)
        val name_arr = this.getResources().getStringArray(R.array.people_names)

        for (item in itemUsuario) {//recorremos la lista de usuario para agregar al de people
            var obj = People()
            obj.image = drw_arr.getResourceId(item.image, -1)
            obj.name = item.name
            obj.email = item.email
            obj.imageDrw = this.getResources().getDrawable(obj.image)
            itemPeople.add(obj)
        }
        //terminado el for mandamos a llamar el adapterpeopleft
        Collections.shuffle(itemPeople)
        adapter = AdapterPeopleLeft(this, itemPeople)
        recyclerView!!.adapter = adapter
    }


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
        menuInflater.inflate(R.menu.menu_basic, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

}
