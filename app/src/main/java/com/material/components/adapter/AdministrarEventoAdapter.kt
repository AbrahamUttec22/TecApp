package com.material.components.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.AppCompatSeekBar
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.alejandrolora.finalapp.inflate
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import kotlinx.android.synthetic.main.activity_form_profile_data.*
import kotlinx.android.synthetic.main.list_view_administrar_eveto.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham
 * Update Delete
 */
class AdministrarEventoAdapter(val context: Context, val layout: Int, val list: List<Evento>) : BaseAdapter() {

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
        val vh: AdministrarEventoViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = AdministrarEventoViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as AdministrarEventoViewHolder
        }
        val titulo = "${list[position].titulo}"
        val description = "${list[position].description}"
        val fecha = "${list[position].fecha}"
        val id_empresa="${list[position].id_empresa}"
        vh.titulo.text = titulo
        val id = "${list[position].id}"

        //only delete
        vh.eliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val evento = Evento()
                evento.id = id
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    sentVoto(evento)
                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        .show()
            }

            private fun sentVoto(evento: Evento) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Eventos")
                //only this source I update the status,
                eventoCollection.document(evento.id).delete().addOnSuccessListener {
                    Toast.makeText(context, "El evento se ha eliminado correctamente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  elimando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })

        //only update
        vh.actualizar.setOnClickListener(object : View.OnClickListener {
            var calendario = Calendar.getInstance()
            override fun onClick(position: View?) {
                var evento = Evento()
                evento.id = id
                evento.id_empresa=id_empresa
                evento.titulo = titulo
                evento.description = description
                evento.fecha = fecha
                showDialog(evento)
            }
            private fun showDialog(eveto: Evento) {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_actualizar_evento)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                //in this code I get the information on cloud firestore
                (dialog.findViewById<View>(R.id.txtTituloEvento) as TextView).text=eveto.titulo
                (dialog.findViewById<View>(R.id.txtDescriptionEvento) as TextView).text=eveto.description
                (dialog.findViewById<View>(R.id.txtFechaEvento) as EditText).setText(eveto.fecha)
                var txt1 = (dialog.findViewById<View>(R.id.txtTituloEvento) as TextView)
                var txt2 = (dialog.findViewById<View>(R.id.txtDescriptionEvento) as TextView)
                var txt3 = (dialog.findViewById<View>(R.id.txtFechaEvento) as EditText)

                //see views front end
                 var date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // TODO Auto-generated method stub
                    calendario.set(Calendar.YEAR, year)
                    calendario.set(Calendar.MONTH, monthOfYear)
                    calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                     val formatoDeFecha = "MM/dd/yy" //In which you need put here
                     val sdf = SimpleDateFormat(formatoDeFecha, Locale.US)
                     txt3.setText(sdf.format(calendario.time))
                }
                txt3.setOnClickListener {
                    DatePickerDialog(context, date, calendario
                            .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                            calendario.get(Calendar.DAY_OF_MONTH)).show()
                }
                //update the event
                (dialog.findViewById<View>(R.id.btnActualizarEvento2) as Button).setOnClickListener {
                    //after that I get the data
                    var tituloNuevo = txt1.text.toString()
                    var descriptionNuevo = txt2.text.toString()
                    var fechaNuevo = txt3.text.toString()
                    if (!tituloNuevo.isNullOrEmpty() && !descriptionNuevo.isNullOrEmpty() && !fechaNuevo.isNullOrEmpty()) {
                        eveto.titulo=tituloNuevo
                        eveto.description=descriptionNuevo
                        eveto.fecha=fechaNuevo
                         updateEvent(eveto)
                        dialog.dismiss()
                        Toast.makeText(context, "El evento se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }

            private fun updateEvent(evento: Evento) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Eventos")
                //only this source I update the status,
                eventoCollection.document(evento.id).update("titulo", evento.titulo,
                        "description", evento.description, "fecha", evento.fecha).addOnSuccessListener {
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder

        })

        return view
    }//end for handler
}

class AdministrarEventoViewHolder(view: View) {
    val titulo: TextView = view.txtTitulo
    val actualizar: Button = view.btnActualizarEvento
    val eliminar: Button = view.btnEliminarEvento
}