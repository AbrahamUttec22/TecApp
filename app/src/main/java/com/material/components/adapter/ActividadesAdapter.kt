package com.material.components.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alejandrolora.finalapp.inflate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.model.Actividades
import com.material.components.model.Evento
import kotlinx.android.synthetic.main.list_view_actividades.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham Casas Aguilar
 */
class ActividadesAdapter(val context: Context, val layout: Int, val list: List<Actividades>) : BaseAdapter() {

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
        val vh: ActividadesViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesViewHolder
        }
        val actividad = "${list[position].actividad}"
        val asignada= "${list[position].fecha_hora_asignada}"
        val terminada="${list[position].fecha_hora_terminada}"

        vh.actividad.text = actividad
        val status = "${list[position].estatus}"
        val id = "${list[position].id}"

        if (status.equals("pendiente")) {
            vh.simpleSwitch.isClickable = true
            vh.simpleSwitch.isChecked = false
            vh.texto.text="Cambiar a realizado"
            vh.fecha_estatus.text="Asignada: "+asignada

        } else if (status.equals("realizado")) {
            vh.simpleSwitch.isChecked = true
            vh.simpleSwitch.isClickable = false
            vh.texto.text="Actividad realizada"
            vh.fecha_estatus.text="Asignada: "+asignada+" \n Concluida: "+terminada

        }

        vh.simpleSwitch.setOnCheckedChangeListener { compoundButton, b ->
            FirebaseApp.initializeApp(context)
            val eventoCollection: CollectionReference
            eventoCollection = FirebaseFirestore.getInstance().collection("Actividades")
            //only this source I update the status,
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("dd/MM/yyyy")
            val fechaA = df.format(c.getTime()).toString()
            val c2 = Calendar.getInstance()
            val df2 = SimpleDateFormat("HH:mm:ss")
            val horaA = df2.format(c2.getTime()).toString()
            val fecha_concluida=fechaA+" "+horaA

            eventoCollection.document(id).update("estatus", "realizado","fecha_hora_terminada",fecha_concluida).addOnSuccessListener {
                Toast.makeText(context, "Se ha cambiado la actividad a realizado", Toast.LENGTH_LONG).show()
                vh.texto.text = "Actividad realizada"
            }.addOnFailureListener { Toast.makeText(context, "Error  al marcar la actividad, intenta de nuevo", Toast.LENGTH_LONG).show() }
        }
        return view
    }

}

class ActividadesViewHolder(view: View) {
    val actividad: TextView = view.txtActividad
    val simpleSwitch: Switch = view.simpleSwitch
    val texto: TextView = view.txtCambiar
    val fecha_estatus: TextView = view.txtFecha

}