package com.material.components.activity.form

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import com.alejandrolora.finalapp.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.EncuestaAdapter
import com.material.components.adapter.EstadisticaAdapter
import com.material.components.model.Encuesta
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.list_view_estadistica.*
import kotlinx.android.synthetic.main.list_view_estadistica.view.*
import java.util.ArrayList

/**
 * @author Abraham
 * This activity is for see staditics for the users
 */
class EstadisticaActivity : AppCompatActivity() {

    private lateinit var adapter: EstadisticaAdapter
    private lateinit var encuestaList: List<Encuesta>

    //declare val for save the collection
    private val userCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        userCollection = FirebaseFirestore.getInstance().collection("Encuestas")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadistica)
        initToolbar()
        addMarksListener()
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
                toast("Ha ocurrido un error intente de nuevo")
            }
        }
    }

    /**
     * @param changes
     * aqui se hace el recorrido de la coleccion de cloudfirestore
     */
    private fun addChanges(changes: List<DocumentChange>) {
        val itemUsuario = ArrayList<Encuesta>()//lista local de una sola instancia
        for (change in changes) {
            itemUsuario.add(change.document.toObject(Encuesta::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        adapter = EstadisticaAdapter(this, R.layout.list_view_estadistica, itemUsuario)
        listView.adapter = adapter
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val pregunta=view!!.txtPregunta.text.toString()
                showErrorDialog()
            }
        }
    }//end for handler

    private fun showErrorDialog() {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_estadistica)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.show()
        dialog.window!!.attributes = lp
    }

    //here the front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Estadisticas")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}//end for handler
