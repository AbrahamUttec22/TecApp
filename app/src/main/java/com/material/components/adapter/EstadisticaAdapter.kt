package com.material.components.adapter
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.material.components.model.Encuesta
import kotlinx.android.synthetic.main.list_view_encuesta.view.*

class EstadisticaAdapter(val context: Context, val layout: Int, val list: List<Encuesta>): BaseAdapter(){

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
        val vh: EncuestaViewHolderTwo
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EncuestaViewHolderTwo(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EncuestaViewHolderTwo
        }
        val fullName = "${list[position].pregunta}"
        vh.pregunta.text = fullName
        return view
    }
}

class EncuestaViewHolderTwo(view: View) {
    val pregunta: TextView = view.txtPregunta
}