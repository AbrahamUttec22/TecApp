package com.material.tecgurus.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.material.tecgurus.model.ImagenProyecto
import kotlinx.android.synthetic.main.list_view_imagen.view.*

class ImagenAdapter(val context: Context, val layout: Int, val imagen: List<ImagenProyecto>) : BaseAdapter(){


    override fun getItem(position: Int): Any {
        return imagen[position]
     }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imagen.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ImagenViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ImagenViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ImagenViewHolder
        }
        //"${list[position].imagen}"
        Glide
                .with(this.context)
                .load("${imagen[position].ubicacion}")
                .into(view.imageView)

        return view
    }
}

private class ImagenViewHolder(view: View) {

}