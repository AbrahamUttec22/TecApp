package com.material.components.activity.form

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatSeekBar
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.alejandrolora.finalapp.goToActivity
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.activity.MainMenu
import com.material.components.adapter.EncuestaAdapter
import com.material.components.adapter.EstadisticaAdapter
import com.material.components.model.Encuesta
import com.material.components.utils.Tools
import kotlinx.android.synthetic.main.activity_agregar_encuesta.*
import kotlinx.android.synthetic.main.activity_encuesta.*
import kotlinx.android.synthetic.main.activity_encuesta.listView
import kotlinx.android.synthetic.main.activity_estadistica.*
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
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    //declare val for save the collection
    private val encuestasCollection: CollectionReference
    private val estadisticasCollection: CollectionReference

    //init the val for get the collection the Firebase with cloud firestore
    init {
        FirebaseApp.initializeApp(this)
        //save the collection marks on val maksCollection
        encuestasCollection = FirebaseFirestore.getInstance().collection("Encuestas")
        estadisticasCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadistica)
        initToolbar()
        addMarksListener()
        swipeRefreshLayout = findViewById(R.id.swipeVerStadisticas)
        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            addMarksListener()
            swipeRefreshLayout!!.setRefreshing(false);

        })
    }

    /**
     * Listener for peopleCollection
     */
    private fun addMarksListener() {
        var sharedPreference = getSharedPreferences ("shared_login_data", Context.MODE_PRIVATE)
        var id_empresa=sharedPreference.getString ("id_empresa","")
        encuestasCollection.whereEqualTo("id_empresa",id_empresa).addSnapshotListener { snapshots, error ->
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
        var con=0
        for (change in changes) {
            con++
            itemUsuario.add(change.document.toObject(Encuesta::class.java))//ir agregando los datos a la lista
        }//una ves agregado los campos mandar a llamar la vista
        if(con==0){
            iconDefaultEstadisticas.setVisibility(View.VISIBLE)
        }else{
            iconDefaultEstadisticas.setVisibility(View.INVISIBLE)
        }
        encuestaList = itemUsuario
        adapter = EstadisticaAdapter(this, R.layout.list_view_estadistica, itemUsuario)
        //listView.btnCerrarEncuesta
        listView.adapter = adapter
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val pregunta = view!!.txtPregunta.text.toString()
                showDialog(pregunta)
            }
        }
    }//end for handler

    private fun showDialog(pregunta: String) {
        //the header from dialog
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_estadistica)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        //in this code I get the information on cloud firestore
        var txt1 = (dialog.findViewById<View>(R.id.txtR1) as TextView)
        var txt2 = (dialog.findViewById<View>(R.id.txtR2) as TextView)
        var txt3 = (dialog.findViewById<View>(R.id.txtR3) as TextView)
        var vt1 = (dialog.findViewById<View>(R.id.txtRS1) as TextView)
        var vt2 = (dialog.findViewById<View>(R.id.txtRS2) as TextView)
        var vt3 = (dialog.findViewById<View>(R.id.txtRS3) as TextView)
        var sk1 = (dialog.findViewById<View>(R.id.seekbar_one) as AppCompatSeekBar)
        var sk2 = (dialog.findViewById<View>(R.id.seekbar_two) as AppCompatSeekBar)
        var sk3 = (dialog.findViewById<View>(R.id.seekbar_three) as AppCompatSeekBar)
        //THE FIRST PASS IS SHOW WHO ANWERS ARE EXISTIS, IF IS TWO O ONLY THREE
        //THIS PASE IS THE END BECAUSE I SHOW THE COUNTS FOR VOT

        var res1 = ""
        var res2 = ""
        var res3 = ""
        var ta = ""
        encuestaList.forEach {
            if (it.pregunta == pregunta) {
                ta = it.respuestas!!.size.toString()
                if (it.respuestas!!.size.toString() == "2") {
                    res1 = it.respuestas!![0]
                    res2 = it.respuestas!![1]
                } else if (it.respuestas!!.size.toString() == "3") {
                    res1 = it.respuestas!![0]
                    res2 = it.respuestas!![1]
                    res3 = it.respuestas!![2]
                }
            }
        }

        //THIS PASE IS THE END BECAUSE I SHOW THE COUNTS FOR VOT
        val resultado = estadisticasCollection.whereEqualTo("id_pregunta", pregunta)
        //beggin with consult
        resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var c1=0
                var c2=0
                var c3=0
                var ctotal=0
                for (document in task.result!!) {
                    val respuesta = document.get("respuesta").toString()
                    if (respuesta== res1) {
                        c1++
                    } else if (respuesta== res2){
                        c2++
                    }else if(respuesta== res3){
                        c3++
                    }
                }//end for
                ctotal=c1+c2+c3
                if (ta == "2") {
                    txt1.text = res1
                    txt2.text = res2
                    vt1.text=c1.toString()
                    vt2.text=c2.toString()
                    sk1.max=ctotal
                    sk2.max=ctotal
                    sk1.progress=c1
                    sk2.progress=c2
                    txt3.setVisibility(View.INVISIBLE)
                    vt3.setVisibility(View.INVISIBLE)
                    sk3.setVisibility(View.INVISIBLE)
                } else if (ta == "3") {
                    txt1.text = res1
                    txt2.text = res2
                    txt3.text = res3
                    sk1.max=ctotal
                    sk2.max=ctotal
                    sk3.max=ctotal
                    sk1.progress=c1
                    sk2.progress=c2
                    sk3.progress=c3
                    vt1.text=c1.toString()
                    vt2.text=c2.toString()
                    vt3.text=c3.toString()
                }
            }
        })//end for expression lambdas this very cool
        //END FOR THE BACKEND ON SHOW
        (dialog.findViewById<View>(R.id.bt_ok) as Button).setOnClickListener { dialog.dismiss() }
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
