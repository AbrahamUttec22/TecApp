package com.material.tecgurus.actividadesfragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

import com.material.tecgurus.R
import com.material.tecgurus.adapter.ARevisionAdapter
import com.material.tecgurus.model.Actividades
import kotlinx.android.synthetic.main.fragment_revision.*
import java.lang.Exception

/**
 * @author Abraham Casas Aguilar
 */
class RevisionFragment : Fragment() {

    private lateinit var adapter: ARevisionAdapter
    //declare val for save the collection
    private val actividadesCollection: CollectionReference
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    // private val channelId = "com.material.components.activity"
    private val channelId = "com.example.vicky.notificationexample"
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //init the val for get the collection the Firebase with cloud firestore
    init {
        //save the collection marks on val maksCollection
        actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revision, container, false)
        listenerDb()
        updateFragment1ListView()
    }

    private fun updateFragment1ListView() {
        if (adapter != null) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listenerDb()
        swipeRefreshLayout = view!!.findViewById(R.id.swipeActividadesRevision)

        swipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            listenerDb()
            swipeRefreshLayout!!.setRefreshing(false)
        })

    }

    private fun addMarksListener() {
        //var sharedPreference = getSharedPreferences("shared_login_data", Context.MODE_PRIVATE)
        // var id_empresa = sharedPreference.getString("id_empresa", "")
        var email = mAuth.currentUser!!.email.toString()
        actividadesCollection.whereEqualTo("correo", email).addSnapshotListener { snapshots, error ->
            if (error == null) {
                val changes = snapshots?.documentChanges
                if (changes != null) {
                    listenerDb()
                }
            } else {
                Toast.makeText(context, "Ha ocurrido un error intenta de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listenerDb() {
        var email = mAuth.currentUser!!.email.toString()
        val consul = actividadesCollection.whereEqualTo("correo", email).whereEqualTo("estatus", "revision")
        //beggin with consult
        try {
            consul.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val itemActividad = ArrayList<Actividades>()//lista local de una sola instanciavar
                    var con = 0
                    for (document in task.result!!) {
                        con++
                        itemActividad.add(document.toObject(Actividades::class.java))//ir agregando los datos a la lista
                    }
                    try {
                        adapter = ARevisionAdapter(context, R.layout.list_view_revision, itemActividad)
                        listViewActividadRevision!!.adapter = adapter
                        if (con == 0) {
                            iconDefaultRevision.setVisibility(View.VISIBLE)
                        } else {
                            iconDefaultRevision.setVisibility(View.INVISIBLE)
                        }
                    } catch (e: Exception) {

                    }

                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })//end for expression lambdas this very cool

        } catch (e: Exception) {

        }
    }


}
