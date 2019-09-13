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
import kotlinx.android.synthetic.main.activity_perfil_empresa.view.*
import kotlinx.android.synthetic.main.dialog_article_comments.view.*
import kotlinx.android.synthetic.main.list_view_panel_empresas.view.*
import kotlinx.android.synthetic.main.list_view_panel_empresas.view.imageEmpresa
import kotlinx.android.synthetic.main.tab1_fragment.view.*

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
        val plan_pago = "${list[position].estatus}"
        val fecha_ve = "${list[position].fecha_vencimiento_plan}"

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
                /*  vh.datos.text = correo + " " + telefono + " Giro: " + giro +
                          " Direccion: " + direccion + " Usuarios en su aplicacion: " + contador.toString() +
                          " Plan de pago: " + plan_pago + " Fecha de registro: " + fecha_registro + " Fecha de vencimiento: " + fecha_ve
                */
                vh.nombreEmpresa.text = nombre
                vh.fechaRegistroEmpresa.text = fecha_registro
                vh.correoEmpresa.text = correo
                vh.telefonoEmpresa.text = telefono
                vh.giroEmpresa.text = giro
                vh.direccionEmpresa.text = direccion
                vh.usuariosEmpresa.text = contador.toString()
                vh.planPagoEmpresa.text = plan_pago
                vh.fechaRegistroEmpresa.text = fecha_registro
                vh.fechaVencimientoPlanEmpresaA.text = fecha_ve

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
    val nombreEmpresa: TextView = view.txtNombreEmpresaA
    val fechaRegistroEmpresa: TextView = view.txtfechaRegistroA
    val correoEmpresa: TextView = view.txtCorreoEmpresaA
    val telefonoEmpresa: TextView = view.txtTelefonoEmpresaA

    val giroEmpresa: TextView = view.txtGiroEmpresaA
    val direccionEmpresa: TextView = view.txtDireccionEmpresaA
    val usuariosEmpresa: TextView = view.txtUsuarioEmpresaA

    val planPagoEmpresa: TextView = view.txtPlanPagoEmpresaA
    val fechaVencimientoPlanEmpresaA: TextView = view.txtfechaVencimeintoPlanEmpresaA

}


