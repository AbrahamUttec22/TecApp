package com.material.components.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import kotlinx.android.synthetic.main.list_view_encuesta.view.*

class EncuestaAdapter(val context: Context, val layout: Int, val list: List<Encuesta>) : BaseAdapter() {

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
        val fullName = "${list[position].pregunta}"
        vh.pregunta.text = fullName
        // vh.respuesta.text = "${list[position].respuestas?.get(position)}"

        var con= list[position].respuestas?.size
        Log.w("CONTADOR",""+con)
        if ( con== 3) {
            vh.respuesta.text = "${list[position].respuestas?.get(0)}"
            vh.respuestatwo.text = "${list[position].respuestas?.get(1)}"
            vh.respuestathree.text = "${list[position].respuestas?.get(2)}"
            vh.respuesta.setVisibility(View.VISIBLE)
            vh.respuestatwo.setVisibility(View.VISIBLE)
            vh.respuestathree.setVisibility(View.VISIBLE)
            con==0
        }else if (con==2){
            vh.respuesta.text = "${list[position].respuestas?.get(0)}"
            vh.respuestatwo.text = "${list[position].respuestas?.get(1)}"
            vh.respuesta.setVisibility(View.VISIBLE)
            vh.respuestatwo.setVisibility(View.VISIBLE)
            vh.respuestathree.setVisibility(View.INVISIBLE)
            con==0
        }else if (con==1){
            vh.respuesta.text = "${list[position].respuestas?.get(0)}"
            con==0
            vh.respuesta.setVisibility(View.VISIBLE)
            vh.respuestatwo.setVisibility(View.VISIBLE)
            vh.respuestathree.setVisibility(View.VISIBLE)
        }
        return view
    }
}
    private class EncuestaViewHolder(view: View) {
        val pregunta: TextView = view.txtPregunta
        val respuesta: TextView = view.txtRespuestas
        val respuestatwo: TextView = view.txtRespuestas2
        val respuestathree: TextView = view.txtRespuestas3
    }