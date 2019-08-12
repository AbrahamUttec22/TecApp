package com.material.components.adapter

import android.app.DatePickerDialog
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
import com.material.components.R
import com.material.components.model.Anuncio
import com.material.components.model.Evento
import kotlinx.android.synthetic.main.list_view_administrar_anuncio.view.*
import kotlinx.android.synthetic.main.list_view_administrar_eveto.view.*
import java.text.SimpleDateFormat
import java.util.*
import android.R.string.cancel
import android.content.DialogInterface
import android.app.AlertDialog


/**
 * @author Abraham
 */
class AdministrarAnuncioAdapter(val context: Context, val layout: Int, val list: List<Anuncio>) : BaseAdapter() {

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
        val vh: AdministrarAnuncioViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = AdministrarAnuncioViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as AdministrarAnuncioViewHolder
        }
        val titulo = "${list[position].titulo}"
        vh.titulo.text = titulo
        val id = "${list[position].id}"
        val description = "${list[position].description}"
        vh.eliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val anuncio = Anuncio()
                anuncio.id = id
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                    sentId(anuncio)
                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        .show()
            }

            private fun sentId(anuncio: Anuncio) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Anuncios")
                //only this source I update the status,
                eventoCollection.document(anuncio.id).delete().addOnSuccessListener {
                    Toast.makeText(context, "El anuncio se ha eliminado correctamente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  elimando el anuncio intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })
        vh.actualizar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                var anuncio = Anuncio()
                anuncio.id = id
                anuncio.description = description
                anuncio.titulo = titulo
                showdialog(anuncio)
            }

            private fun showdialog(anuncio: Anuncio) {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_actualizar_anuncio)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                //in this code I get the information on cloud firestore
                (dialog.findViewById<View>(R.id.txtTituloAnuncio) as TextView).text = anuncio.titulo
                (dialog.findViewById<View>(R.id.txtDescriptionAnuncio) as TextView).text = anuncio.description
                var txt1 = (dialog.findViewById<View>(R.id.txtTituloAnuncio) as TextView)
                var txt2 = (dialog.findViewById<View>(R.id.txtDescriptionAnuncio) as TextView)

                (dialog.findViewById<View>(R.id.btnActualizarAnuncio2) as Button).setOnClickListener {
                    //after that I get the data
                    var tituloNuevo = txt1.text.toString()
                    var descriptionNuevo = txt2.text.toString()
                    if (!tituloNuevo.isNullOrEmpty() && !descriptionNuevo.isNullOrEmpty()) {
                        anuncio.titulo = tituloNuevo
                        anuncio.description = descriptionNuevo
                        updateAnuncio(anuncio)
                        dialog.dismiss()
                        Toast.makeText(context, "El anuncio se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }

            private fun updateAnuncio(anuncio: Anuncio) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Anuncios")
                //only this source I update the status,
                eventoCollection.document(anuncio.id).update("titulo", anuncio.titulo,
                        "description", anuncio.description).addOnSuccessListener {
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })

        return view
    }
}

class AdministrarAnuncioViewHolder(view: View) {
    val titulo: TextView = view.txtAnuncio
    val actualizar: Button = view.btnActualizarAnuncio
    val eliminar: Button = view.btnEliminarAnuncio
}