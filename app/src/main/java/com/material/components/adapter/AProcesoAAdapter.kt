package com.material.components.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.alejandrolora.finalapp.inflate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.model.Actividades
import kotlinx.android.synthetic.main.list_view_actividades_admin.view.*
import kotlinx.android.synthetic.main.list_view_proceso.view.*
import kotlinx.android.synthetic.main.list_view_proceso_admin.view.*

/**
 * @author Abraham Casas Aguilar
 */
class AProcesoAAdapter(val context: Context?, val layout: Int, val list: List<Actividades>) : BaseAdapter() {


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ActividadesViewHolderTwoAdmin
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesViewHolderTwoAdmin(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesViewHolderTwoAdmin
        }

        val status = "${list[position].estatus}"//no mostrar
        val id = "${list[position].id}"//no mostrar id del documento
        var correo = "${list[position].descripcion}"//no mostrar
        var id_usuario = "${list[position].id_usuario}"//no mostrar
        var id_empresa = "${list[position].id_empresa}"//no mostrar
        var email_asigno = "${list[position].email_asigno}"//Actividad Asignada por
        var email = "${list[position].correo}"//Actividad Asignada por


        var titulo = "${list[position].actividad}"//mostrar
        var descripcion = "${list[position].descripcion}"//mostrar
        var fecha_compromiso = "${list[position].fecha_compromiso}"//mostrar

        vh.actividadTwo.text = titulo
        vh.descripcionTwo.text = descripcion
        vh.fechaacTwo.text = "Fecha Compromiso: " + fecha_compromiso


        val userCollection: CollectionReference
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        val empleado = userCollection.whereEqualTo("email", email_asigno).whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    vh.info.text = "Le has asignado esta actividad a: " + document.get("name")
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool


        val empresaCollection: CollectionReference
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        val empresa = empresaCollection.whereEqualTo("correo", email_asigno).whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    vh.info.text = "Le has asignado esta actividad a: " + document.get("nombre")
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool


        vh.mover.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                var activid = Actividades()
                activid.id = id
                updateActividad(activid)
            }

            private fun updateActividad(actividad: Actividades) {
                if (context != null) {
                    FirebaseApp.initializeApp(context)
                }
                val actividadesCollection: CollectionReference
                actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //only this source I update the status,
                actividadesCollection.document(actividad.id).update("estatus", "proceso").addOnSuccessListener {
                    Toast.makeText(context, "Se ha movido la actividad a: En Proceso", Toast.LENGTH_LONG).show()

                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })
        return view
    }


}

class ActividadesViewHolderTwoAdmin(view: View) {
    val actividadTwo: TextView = view.txtActividadProcesoAdmin
    val descripcionTwo: TextView = view.txtDescripcionAcProcesoAdmin
    val info: TextView = view.txInfoAcProcesoAdmin
    val fechaacTwo: TextView = view.txtFechaActiviProcesoAdmin
    val mover: Button = view.moverrevisionAdmin
}