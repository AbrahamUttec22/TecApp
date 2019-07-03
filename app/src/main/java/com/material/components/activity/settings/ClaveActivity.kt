package com.material.components.activity.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alejandrolora.finalapp.toast
import com.alejandrolora.finalapp.validate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.adapter.EventoAdapter
import com.material.components.model.Clave
import com.material.components.model.Evento
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_card_basic.*
import kotlinx.android.synthetic.main.activity_clave.*
import java.util.ArrayList

/**
 * @author Abraham
 */
class ClaveActivity : AppCompatActivity() {

    private val claveCollection: CollectionReference
    private var claveAcceso: String = ""
    private var idDocument: String = ""

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        claveCollection = FirebaseFirestore.getInstance().collection("registros")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clave)
        initToolbar()
        addMarksListener()
        btnUpdateClave.setOnClickListener {
            val clave = txtClaveNueva.text.toString()
            val confirmClave = txtConfirmClave.text.toString()
            if (isValidated(clave, confirmClave) && isValidConfirmClave(clave, confirmClave)) {
                updateClave(confirmClave)
            } else {
                toast("Completa los campos")
            }
        }//end for listener

        txtConfirmClave.validate {
            txtConfirmClave.error = if (isValidConfirmClave(txtClaveNueva.text.toString(), it)) null else "Las claves no coinciden"
        }
    }

    private fun updateClave(clave: String) {
//add the collection and save the User, this is validated
        claveCollection.document(idDocument).update("acceso", clave).addOnSuccessListener {
            Toast.makeText(this, "Clave actualizada", Toast.LENGTH_LONG).show()
            onBackPressed()
        }.addOnFailureListener {
            Toast.makeText(this, "Error guardando el evento, intenta de nuevo", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        claveCollection.addSnapshotListener { snapshots, error ->
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
        val itemClave = ArrayList<Clave>()//lista local de una sola instancia
        for (change in changes) {
            itemClave.add(change.document.toObject(Clave::class.java))//ir agregando los datos a la lista
            //whit this code I get id for document
            idDocument = change.document.id
        }//una ves agregado los campos mandar a llamar la vista
        claveAcceso = "${itemClave[0].acceso}"
        valorClaveAcceso.text = claveAcceso
    }


    private fun isValidConfirmClave(clave: String, confirmClave: String): Boolean {
        return clave == confirmClave
    }

    //backend
    private fun isValidated(clave: String, confirmClave: String): Boolean {
        return !clave.isNullOrEmpty() &&
                !confirmClave.isNullOrEmpty()
    }

    //front end
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbarAcceso) as Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_basic, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
