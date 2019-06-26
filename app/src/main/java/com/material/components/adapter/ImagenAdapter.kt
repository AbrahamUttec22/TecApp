package com.material.components.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.alejandrolora.finalapp.inflate
import kotlinx.android.synthetic.main.list_view_imagen.view.*

class ImagenAdapter(val context: Context, val layout: Int, val imagenes: Array<String>) : BaseAdapter(){


    override fun getItem(position: Int): Any {
        return imagenes[position]
     }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imagenes.size
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
        vh.imagen.setImageResource(imagenes.get(position).toInt())
        return view
    }
}

private class ImagenViewHolder(view: View) {
    val imagen: ImageView = view.imageView

}