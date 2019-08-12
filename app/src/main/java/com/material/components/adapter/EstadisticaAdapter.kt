package com.material.components.adapter
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.alejandrolora.finalapp.inflate
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.model.Encuesta
import com.material.components.model.Votacion
import kotlinx.android.synthetic.main.list_view_encuesta.view.*
import kotlinx.android.synthetic.main.list_view_encuesta.view.txtPregunta
import kotlinx.android.synthetic.main.list_view_estadistica.view.*
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.R.attr.button
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Color.parseColor
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.material.components.R
import com.material.components.model.Evento
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham
 */
class EstadisticaAdapter(val context: Context, val layout: Int, val list: List<Encuesta>): BaseAdapter(){

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
        val vh: EncuestaViewHolderTwo
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EncuestaViewHolderTwo(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EncuestaViewHolderTwo
        }
        val fullName = "${list[position].pregunta}"
        vh.pregunta.text = fullName
        val status="${list[position].status}"

        if(status=="1"){//el evento esta aun abierto, por lo tanto permite cerrar

        }else{//ele vento esta cerrado se debe deshabilitar el boton
            val color = Color.parseColor("#999999")
            vh.cerrar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
            vh.cerrar.setVisibility(View.INVISIBLE)
        }

        vh.cerrar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val pregunta = vh.pregunta.text.toString()
                val encuesta = Encuesta()
                encuesta.pregunta = pregunta
                encuesta.status="0"//false
                sentVoto(encuesta)
            }
            private fun sentVoto(encuesta: Encuesta) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Encuestas")
                //only this source I update the status,
                eventoCollection.document(encuesta.pregunta).update("status", encuesta.status).addOnSuccessListener {
                    Toast.makeText(context, "La encuesta se ha cerrado correctamente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  cerrando la encuesta intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })

        //this case if for drop the encuest
        vh.eliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val pregunta = vh.pregunta.text.toString()
                val encuesta = Encuesta()
                encuesta.pregunta = pregunta
                encuesta.status="0"//false
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                    sentVoto(encuesta)
                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        .show()
            }
            private fun sentVoto(encuesta: Encuesta) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Encuestas")
                //only this source I update the status,
                eventoCollection.document(encuesta.pregunta).delete().addOnSuccessListener {
                    Toast.makeText(context, "La encuesta se ha eliminado correctamente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  cerrando la encuesta intenta de nuevo", Toast.LENGTH_LONG).show() }

            }//end for hanlder
        })

        return view
    }
}

class EncuestaViewHolderTwo(view: View) {
    val pregunta: TextView = view.txtPregunta
    val cerrar: Button = view.btnCerrarEncuesta
    val eliminar: Button = view.btnEliminarEncuesta
}