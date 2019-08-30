package com.material.components.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.alejandrolora.finalapp.inflate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.model.Actividades
import kotlinx.android.synthetic.main.list_view_actividades.view.*
import kotlinx.android.synthetic.main.list_view_proceso.view.*
import java.util.*

class AProcesoAdapter(val context: Context?, val layout: Int, val list: List<Actividades>) : BaseAdapter() {

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
        val vh: ActividadesViewHolderTwo
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesViewHolderTwo(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesViewHolderTwo
        }

        val status = "${list[position].estatus}"//no mostrar
        val id = "${list[position].id}"//no mostrar id del documento
        var correo = "${list[position].descripcion}"//no mostrar
        var id_usuario = "${list[position].id_usuario}"//no mostrar
        var id_empresa = "${list[position].id_empresa}"//no mostrar
        var email_asigno = "${list[position].email_asigno}"//Actividad Asignada por


        var titulo = "${list[position].actividad}"//mostrar
        var descripcion = "${list[position].descripcion}"//mostrar
        var fecha_compromiso = "${list[position].fecha_compromiso}"//mostrar

        vh.actividadTwo.text = titulo
        vh.descripcionTwo.text = descripcion
        vh.fechaacTwo.text = fecha_compromiso

        vh.mover.setOnClickListener(object : View.OnClickListener {
            var calendario = Calendar.getInstance()
            override fun onClick(position: View?) {
                var activid = Actividades()
                activid.id = id
                updateActividad(activid)
            }

            private fun updateActividad(actividad: Actividades) {
                if (context != null) {
                    FirebaseApp.initializeApp(context)
                }else{
                }
                val actividadesCollection: CollectionReference
                actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //only this source I update the status,
                actividadesCollection.document(actividad.id).update("estatus", "revision").addOnSuccessListener {
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })

        return view
    }


}

class ActividadesViewHolderTwo(view: View) {
    val actividadTwo: TextView = view.txtActividadProceso
    val descripcionTwo: TextView = view.txtDescripcionAcProceso
    val fechaacTwo: TextView = view.txtFechaActiviProceso
    val mover: Button = view.moverrevision


}