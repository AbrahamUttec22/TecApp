package com.material.tecgurus.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.model.Checador
import kotlinx.android.synthetic.main.list_view_estatus_checador.view.*
import kotlinx.android.synthetic.main.list_view_estatus_checador.view.imageUser

/**
 * @author Abraham Casas Aguilar
 */
class EstatusChecadorAdapter(val context: Context, val layout: Int, val list: List<Checador>) : BaseAdapter() {

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
        val vh: EstatusChecadorViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EstatusChecadorViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EstatusChecadorViewHolder
        }
        val id_usuario = "${list[position].id_usuario}"
        val id = "${list[position].id}"
        //val id_empresa = "${list[position].id_empresa}"
        val fecha = "${list[position].fecha}"


        vh.fechaChecador.text = fecha
        vh.horaChecador.text = "${list[position].hora}"

        FirebaseApp.initializeApp(context)
        val usuarioCollection: CollectionReference
        val checadorCollection: CollectionReference
        usuarioCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        // checadorCollection = FirebaseFirestore.getInstance().collection("Checador")

        try {
            //only this source I update the status,
            val consulta = usuarioCollection.whereEqualTo("id", id_usuario)
            //beggin with consult
            consulta.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val nombreEmpleado = document.get("name").toString()
                        val ubicacion = document.get("ubicacion").toString()
                        vh.nombreEmpleado.text = nombreEmpleado
                        Glide
                                .with(this.context)
                                .load(ubicacion)
                                .into(view.imageUser)
                    }
                } else {
                    Log.w("saasas", "Error getting documents.", task.exception)
                }
            })//end for expression lambdas this very cool

        } catch (e: java.lang.Exception) {
        }


        /*     val consultaChecador = checadorCollection.whereEqualTo("fecha", fecha).
                     whereEqualTo("id_usuario", id_usuario).whereEqualTo("id_empresa", id_empresa)
             //beggin with consult
             consultaChecador.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                 if (task.isSuccessful) {
                     var horasEmpleado=""
                     for (document in task.result!!) {
                         horasEmpleado = document.get("hora").toString()+""
                     }
                     vh.horaChecador.text=horasEmpleado

                 } else {
                     Log.w("saasas", "Error getting documents.", task.exception)
                 }
             })//end for expression lambdas this very cool

     */

        return view
    }

}

class EstatusChecadorViewHolder(view: View) {
    val nombreEmpleado: TextView = view.txtNombreEmpleado
    val fechaChecador: TextView = view.txtFechaChecador
    val horaChecador: TextView = view.txtHoraChecador
}