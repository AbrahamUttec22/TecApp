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
import com.material.tecgurus.model.Pagos
import kotlinx.android.synthetic.main.list_view_pagos_all.view.*
import kotlinx.android.synthetic.main.list_view_panel_empresas.view.*

/**
 * @author Abraham
 */
class PagosAllAdapter(val context: Context, val layout: Int, val list: List<Pagos>) : BaseAdapter() {


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
        val vh: PagosAllViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = PagosAllViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as PagosAllViewHolder
        }
        val estatus = "${list[position].estatus}"
        val fecha_pago = "${list[position].fecha_pago}"
        val hora_pago = "${list[position].hora_pago}"
        val id = "${list[position].id}"
        val id_empresa = "${list[position].id_empresa}"
        val id_pago_paypal = "${list[position].id_pago_paypal}"
        val monto = "${list[position].monto}"//no mostrar

        val empresaCollection: CollectionReference
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        val empresa = empresaCollection.whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var nombre_empresa = ""
                for (document in task.result!!) {
                    nombre_empresa = document.get("nombre").toString()
                }

                vh.nombreEmpresa.text = nombre_empresa
                vh.fechaPago.text = fecha_pago
                vh.horaPago.text = hora_pago
                vh.montoPago.text = monto
                vh.estatusPago.text = estatus
                vh.idPagoPaypal.text = id_pago_paypal

            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool

        //  vh.descipcion.text = "Giro: " + giro + " Direccion: " + direccion
        //  vh.descipciontwo.text = "Usuarios en su aplicacion: " + contador.toString() + " Plan de pago: Prueba de 15 días"

        return view
    }


}

class PagosAllViewHolder(view: View) {
    val nombreEmpresa: TextView = view.txtNombreEmpresaPagoA
    val fechaPago: TextView = view.txtFechaPagoA
    val horaPago: TextView = view.txtHoraPagoA
    val montoPago: TextView = view.txtMontoPagoA
    val estatusPago: TextView = view.txtEstatusPaggoA
    val idPagoPaypal: TextView = view.txtIdPagoPaypalA
}

