package com.material.components.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import kotlinx.android.synthetic.main.list_view_encuesta.view.*

class EncuestaAdapter (val context: Context, val layout: Int, val list: List<Encuesta>) : BaseAdapter() {

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
        val vh: EncuestaViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EncuestaViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EncuestaViewHolder
        }

        val fullName = "${list[position].titulo}"
        vh.fullName.text = fullName
        vh.age.text = "${list[position].description}"

        return view

    }


}

private class EncuestaViewHolder(view: View) {
    val fullName: TextView = view.textViewName
    val age: TextView = view.textViewAge
}