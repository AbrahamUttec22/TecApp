package com.material.components.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.material.components.model.Usuario
import kotlinx.android.synthetic.main.list_view_usuario.view.*

class UserAdapter(val context: Context, val layout: Int, val list: List<Usuario>) : BaseAdapter() {

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
        val vh: UserViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = UserViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as UserViewHolder
        }

        val fullName = "${list[position].name}"
        vh.fullName.text = fullName
        Glide
                .with(this.context)
                .load("${list[position].ubicacion}")
                .into(view.imageUser)
        return view
    }


}

private class UserViewHolder(view: View) {
    val fullName: TextView = view.textViewName
}