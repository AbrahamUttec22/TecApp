package com.material.tecgurus.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.model.Empresa
import kotlinx.android.synthetic.main.list_view_panel_empresas.view.*

/**
 * @author Abraham Casas Aguilar
 */

class PanelEmpresasAdapter(val context: Context, val layout: Int, val list: List<Empresa>) : BaseAdapter() {


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
        val vh: EmpresasListViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EmpresasListViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EmpresasListViewHolder
        }
        val uid = "${list[position].uid}"//no mostrar
        val token = "${list[position].token}"//no mostrar
        val id_empresa = "${list[position].id_empresa}"//no mostrar

        val giro = "${list[position].giro}"//mostrar
        val direccion = "${list[position].direccion}"//mostrar

        val foto = "${list[position].foto}"
        val nombre = "${list[position].nombre}"
        val telefono = "${list[position].telefono}"
        val correo = "${list[position].correo}"
        val fecha_registro = "${list[position].fecha_registro}"


        Glide
                .with(this.context)
                .load(foto)
                .into(view.imageEmpresa)

        var contador = 0
        val userCollection: CollectionReference
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        val empleado = userCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    contador++
                }
                vh.nombre.text = nombre
                vh.datos.text = correo + " " + telefono + " Giro: " + giro +
                        " Direccion: " + direccion + " Usuarios en su aplicacion: "+contador.toString() +
                        " Plan de pago: Prueba de 15 días" + " Fecha de registro: " + fecha_registro

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool

        //  vh.descipcion.text = "Giro: " + giro + " Direccion: " + direccion
        //  vh.descipciontwo.text = "Usuarios en su aplicacion: " + contador.toString() + " Plan de pago: Prueba de 15 días"

        return view
    }

}

class EmpresasListViewHolder(view: View) {
    val nombre: TextView = view.textViewNameEmpresas
    val datos: TextView = view.textDatosEmpresa
}


