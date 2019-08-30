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
import kotlinx.android.synthetic.main.list_view_revision.view.*
import java.util.*

class ARevisionAdapter(val context: Context?, val layout: Int, val list: List<Actividades>) : BaseAdapter() {


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
        val vh: ActividadesViewHolderThree
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesViewHolderThree(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesViewHolderThree
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

        vh.actividadThree.text = titulo
        vh.descripcionThree.text = descripcion
        vh.fechaacThree.text = fecha_compromiso

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
                }
                val actividadesCollection: CollectionReference
                actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //only this source I update the status,
                actividadesCollection.document(actividad.id).update("estatus", "finalizado").addOnSuccessListener {
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })

        vh.moverre.setOnClickListener(object : View.OnClickListener {
            var calendario = Calendar.getInstance()
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
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })

        return view
    }


}

class ActividadesViewHolderThree(view: View) {
    val actividadThree: TextView = view.txtActividadRevision
    val descripcionThree: TextView = view.txtDescripcionAcRevision
    val fechaacThree: TextView = view.txtFechaActiviRevision
    val mover: Button = view.moverfinalizado
    val moverre: Button = view.moverprocesoTwo


}