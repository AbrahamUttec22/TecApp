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
import android.support.v7.widget.AppCompatSeekBar
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.material.components.R
import com.material.components.model.Evento
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham
 */
class EstadisticaAdapter(val context: Context, val layout: Int, val list: List<Encuesta>) : BaseAdapter() {

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
        val status = "${list[position].status}"

        if (status == "1") {//el evento esta aun abierto, por lo tanto permite cerrar
            vh.textocerrar.text = "Cerrar"
        } else {//ele vento esta cerrado se debe deshabilitar el boton
            //val color = Color.parseColor("#999999")
            //   vh.cerrar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
            vh.cerrar.setVisibility(View.INVISIBLE)
            vh.textocerrar.text = "La encuesta se ha cerrado"
        }

        try {
            vh.cerrar.setOnClickListener(object : View.OnClickListener {
                override fun onClick(position: View?) {
                    val pregunta = vh.pregunta.text.toString()
                    val encuesta = Encuesta()
                    encuesta.pregunta = pregunta
                    encuesta.status = "0"//false
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Estas seguro de cerrar la encuesta?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                        sentVoto(encuesta)
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                            .show()
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
                    encuesta.status = "0"//false
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
            vh.progreso.setOnClickListener(object : View.OnClickListener {
                override fun onClick(position: View?) {
                    showDialog(fullName)
                }

                private fun showDialog(pregunta: String) {
                    //the header from dialog
                    val dialog = Dialog(context)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                    dialog.setContentView(R.layout.dialog_estadistica)
                    dialog.setCancelable(true)
                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                    //in this code I get the information on cloud firestore
                    var txt1 = (dialog.findViewById<View>(R.id.txtR1) as TextView)
                    var txt2 = (dialog.findViewById<View>(R.id.txtR2) as TextView)
                    var txt3 = (dialog.findViewById<View>(R.id.txtR3) as TextView)
                    var vt1 = (dialog.findViewById<View>(R.id.txtRS1) as TextView)
                    var vt2 = (dialog.findViewById<View>(R.id.txtRS2) as TextView)
                    var vt3 = (dialog.findViewById<View>(R.id.txtRS3) as TextView)
                    var sk1 = (dialog.findViewById<View>(R.id.seekbar_one) as AppCompatSeekBar)
                    var sk2 = (dialog.findViewById<View>(R.id.seekbar_two) as AppCompatSeekBar)
                    var sk3 = (dialog.findViewById<View>(R.id.seekbar_three) as AppCompatSeekBar)
                    //THE FIRST PASS IS SHOW WHO ANWERS ARE EXISTIS, IF IS TWO O ONLY THREE
                    //THIS PASE IS THE END BECAUSE I SHOW THE COUNTS FOR VOT

                    var res1 = ""
                    var res2 = ""
                    var res3 = ""
                    var ta = ""
                    list.forEach {
                        if (it.pregunta == pregunta) {
                            ta = it.respuestas!!.size.toString()
                            if (it.respuestas!!.size.toString() == "2") {
                                res1 = it.respuestas!![0]
                                res2 = it.respuestas!![1]
                            } else if (it.respuestas!!.size.toString() == "3") {
                                res1 = it.respuestas!![0]
                                res2 = it.respuestas!![1]
                                res3 = it.respuestas!![2]
                            }
                        }
                    }

                    FirebaseApp.initializeApp(context)
                    val encuestaCollection: CollectionReference
                    val estadisticasCollection: CollectionReference

                    encuestaCollection = FirebaseFirestore.getInstance().collection("Encuestas")
                    estadisticasCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
                    //THIS PASE IS THE END BECAUSE I SHOW THE COUNTS FOR VOT
                    val resultado = estadisticasCollection.whereEqualTo("id_pregunta", pregunta)
                    //beggin with consult
                    resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                        if (task.isSuccessful) {
                            var c1 = 0
                            var c2 = 0
                            var c3 = 0
                            var ctotal: Int
                            for (document in task.result!!) {
                                val respuesta = document.get("respuesta").toString()
                                if (respuesta == res1) {
                                    c1++
                                } else if (respuesta == res2) {
                                    c2++
                                } else if (respuesta == res3) {
                                    c3++
                                }
                            }//end for
                            ctotal = c1 + c2 + c3
                            if (ta == "2") {
                                txt1.text = res1
                                txt2.text = res2
                                vt1.text = c1.toString()
                                vt2.text = c2.toString()
                                sk1.max = ctotal
                                sk2.max = ctotal
                                sk1.progress = c1
                                sk2.progress = c2
                                txt3.setVisibility(View.INVISIBLE)
                                vt3.setVisibility(View.INVISIBLE)
                                sk3.setVisibility(View.INVISIBLE)
                            } else if (ta == "3") {
                                txt1.text = res1
                                txt2.text = res2
                                txt3.text = res3
                                sk1.max = ctotal
                                sk2.max = ctotal
                                sk3.max = ctotal
                                sk1.progress = c1
                                sk2.progress = c2
                                sk3.progress = c3
                                vt1.text = c1.toString()
                                vt2.text = c2.toString()
                                vt3.text = c3.toString()
                            }
                        }
                    })//end for expression lambdas this very cool
                    //END FOR THE BACKEND ON SHOW
                    (dialog.findViewById<View>(R.id.bt_ok) as Button).setOnClickListener { dialog.dismiss() }
                    dialog.show()
                    dialog.window!!.attributes = lp
                }
            })
        } catch (e: java.lang.Exception) {
        }
        return view
    }
}

class EncuestaViewHolderTwo(view: View) {
    val pregunta: TextView = view.txtPregunta
    val cerrar: ImageButton = view.btnCerrarEncuesta
    val eliminar: ImageButton = view.btnEliminarEncuesta
    val progreso: ImageButton = view.btnVerResultados
    val textocerrar: TextView = view.txtcerrarEncu


}