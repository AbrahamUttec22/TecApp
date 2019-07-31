package com.material.components.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.material.components.model.Actividades
import kotlinx.android.synthetic.main.list_view_actividades.view.*
import kotlinx.android.synthetic.main.list_view_actividades_ver.view.*

/**
 * @author Abraham Casas Aguilar
 */
class ActividadesVerAdapter(val context: Context, val layout: Int, val list: List<Actividades>) : BaseAdapter() {

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
        val vh: ActividadesVerViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesVerViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesVerViewHolder
        }
        val status = "${list[position].estatus}"
        val actividad = "${list[position].actividad}"

        if (status.equals("pendiente")) {
            vh.simpleSwitch.isChecked = false
            vh.simpleSwitch.isClickable = false
            vh.texto.text = "Pendiente"
            vh.actividad.text=actividad
        } else if (status.equals("realizado")) {
            vh.actividad.text=actividad
            vh.simpleSwitch.isChecked = true
            vh.simpleSwitch.isClickable = false
            vh.texto.text = "Actividad realizada"
        }

        return view
    }

}

class ActividadesVerViewHolder(view: View) {
    val actividad: TextView = view.txtActividadDialog
    val simpleSwitch: Switch = view.simpleSwitchDialog
    val texto: TextView = view.txtCambiarDialog
}


