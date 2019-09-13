package com.material.tecgurus.adapter

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.alejandrolora.finalapp.inflate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.tecgurus.R
import com.material.tecgurus.model.Costos
import kotlinx.android.synthetic.main.list_view_costos_anuales.view.*
import kotlinx.android.synthetic.main.list_view_costos_mensuales.view.*

/**
 * Created by:
 * @author Abraham Casas Aguilar
 */
class CostosAnualesAdapter(val context: Context, val layout: Int, val list: List<Costos>) : BaseAdapter() {

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
        val vh: CostosViewHolderTwo
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = CostosViewHolderTwo(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as CostosViewHolderTwo
        }

        try {
            val id = "${list[position].id}"
            val plan = "${list[position].plan}"
            val costo = "${list[position].costo}"
            vh.plan.text = plan
            vh.costo.text = costo
            vh.actualizarCosto.setOnClickListener(object : View.OnClickListener {
                override fun onClick(position: View?) {
                    var costos = Costos()
                    costos.id = id
                    costos.plan = plan
                    costos.costo = costo
                    showdialog(costos)
                }

                private fun showdialog(costo: Costos) {
                    //the header from dialog
                    val dialog = Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                    dialog.setContentView(R.layout.dialog_actualizar_costo)
                    dialog.setCancelable(true)
                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                    //in this code I get the information on cloud firestore

                    (dialog.findViewById<View>(R.id.txtCosto) as TextView).text = costo.costo
                    var txt1 = (dialog.findViewById<View>(R.id.txtCosto) as TextView)

                    (dialog.findViewById<View>(R.id.btnActualizarCost) as Button).setOnClickListener {
                        //after that I get the data
                        var nuevoCosto = txt1.text.toString()
                        if (!nuevoCosto.isNullOrEmpty()) {
                            costo.costo = nuevoCosto
                            updateCosto(costo)
                            dialog.dismiss()
                            Toast.makeText(context, "El costo de ha actualizaco correctamente", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.show()
                    dialog.window!!.attributes = lp
                }

                private fun updateCosto(costo: Costos) {
                    FirebaseApp.initializeApp(context)
                    val costosCollection: CollectionReference
                    costosCollection = FirebaseFirestore.getInstance().collection("Costos")
                    //only this source I update the status,
                    costosCollection.document(costo.id).update("costo", costo.costo).addOnSuccessListener {
                    }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
                }//end for hanlder
            })
            vh.actualizarCostoTwo.setOnClickListener(object : View.OnClickListener {
                override fun onClick(position: View?) {
                    var costos = Costos()
                    costos.id = id
                    costos.plan = plan
                    costos.costo = costo
                    showdialog(costos)
                }

                private fun showdialog(costo: Costos) {
                    //the header from dialog
                    val dialog = Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                    dialog.setContentView(R.layout.dialog_actualizar_costo)
                    dialog.setCancelable(true)
                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                    //in this code I get the information on cloud firestore

                    (dialog.findViewById<View>(R.id.txtCosto) as TextView).text = costo.costo
                    var txt1 = (dialog.findViewById<View>(R.id.txtCosto) as TextView)

                    (dialog.findViewById<View>(R.id.btnActualizarCost) as Button).setOnClickListener {
                        //after that I get the data
                        var nuevoCosto = txt1.text.toString()
                        if (!nuevoCosto.isNullOrEmpty()) {
                            costo.costo = nuevoCosto
                            updateCosto(costo)
                            dialog.dismiss()
                            Toast.makeText(context, "El costo de ha actualizaco correctamente", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.show()
                    dialog.window!!.attributes = lp
                }

                private fun updateCosto(costo: Costos) {
                    FirebaseApp.initializeApp(context)
                    val costosCollection: CollectionReference
                    costosCollection = FirebaseFirestore.getInstance().collection("Costos")
                    //only this source I update the status,
                    costosCollection.document(costo.id).update("costo", costo.costo).addOnSuccessListener {
                    }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
                }//end for hanlder
            })

        } catch (e: java.lang.Exception) {
        }

        return view
    }

}


class CostosViewHolderTwo(view: View) {
    val plan: TextView = view.txtTipoPlanAnual
    val costo: TextView = view.txtCostoAnual
    val actualizarCosto: ImageButton = view.btnActualizarCostoAnual
    val actualizarCostoTwo: TextView = view.btnActualizarCostoAnualTwo

}
