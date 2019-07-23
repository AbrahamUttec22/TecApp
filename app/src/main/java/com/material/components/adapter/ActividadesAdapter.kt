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
        vh.actividad.text = actividad
        val status = "${list[position].estatus}"
        val id = "${list[position].id}"
        if (status.equals("pendiente")) {
            vh.simpleSwitch.isClickable = true
            vh.simpleSwitch.isChecked = false
            vh.texto.text="Cambiar a realizado"
            val color = Color.parseColor("#999999")
            vh.eliminar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
            vh.eliminar.setVisibility(View.INVISIBLE)

        } else if (status.equals("realizado")) {
            vh.simpleSwitch.isChecked = true
            vh.simpleSwitch.isClickable = false
            vh.texto.text="Actividad realizada"

        }

        vh.simpleSwitch.setOnCheckedChangeListener { compoundButton, b ->
            FirebaseApp.initializeApp(context)
            val eventoCollection: CollectionReference
            eventoCollection = FirebaseFirestore.getInstance().collection("Actividades")
            //only this source I update the status,
            eventoCollection.document(id).update("estatus", "realizado").addOnSuccessListener {
                Toast.makeText(context, "Se ha cambiado la actividad a realizado", Toast.LENGTH_LONG).show()
                vh.texto.text = "Actividad realizada"
            }.addOnFailureListener { Toast.makeText(context, "Error  al marcar la actividad, intenta de nuevo", Toast.LENGTH_LONG).show() }
        }
        vh.eliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val actividad = Actividades()
                actividad.id = id
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    sentVoto(actividad)
                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        .show()
            }

            private fun sentVoto(actividad: Actividades) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //only this source I update the status,
                eventoCollection.document(actividad.id).delete().addOnSuccessListener {
                    Toast.makeText(context, "Se ha eliminado correctamente la actividad", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  elimando la actividad intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })


        return view
    }

}

class ActividadesViewHolder(view: View) {
    val actividad: TextView = view.txtActividad
    val simpleSwitch: Switch = view.simpleSwitch
    val texto: TextView = view.txtCambiar
    val eliminar: Button = view.btnEliminarActividad


}