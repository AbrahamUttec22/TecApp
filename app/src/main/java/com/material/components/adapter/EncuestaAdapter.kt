package com.material.components.adapter

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.alejandrolora.finalapp.inflate
import com.alejandrolora.finalapp.toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.R
import com.material.components.activity.dialog.EncuestaActivity
import com.material.components.activity.login.LoginCardOverlap
import com.material.components.model.Encuesta
import com.material.components.model.Evento
import com.material.components.model.Votacion
import kotlinx.android.synthetic.main.list_view_encuesta.view.*

/**
 * @author Abraham
 * this source I send the voto for the encuesta
 */
class EncuestaAdapter(val context: Context, val layout: Int, val list: List<Encuesta>) : BaseAdapter() {

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
        val vh: EncuestaViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = EncuestaViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as EncuestaViewHolder
        }
        val fullName = "${list[position].pregunta}"
        val id_empresa = "${list[position].id_empresa}"

        vh.pregunta.text = fullName
        // vh.respuesta.text = "${list[position].respuestas?.get(position)}"
        var con = list[position].respuestas?.size
        Log.w("CONTADOR", "" + con)
        if (con == 3) {
            vh.respuesta.text = "${list[position].respuestas?.get(0)}"
            vh.respuestatwo.text = "${list[position].respuestas?.get(1)}"
            vh.respuestathree.text = "${list[position].respuestas?.get(2)}"
            vh.respuesta.setVisibility(View.VISIBLE)
            vh.respuestatwo.setVisibility(View.VISIBLE)
            vh.respuestathree.setVisibility(View.VISIBLE)
            con == 0
        } else if (con == 2) {
            vh.respuesta.text = "${list[position].respuestas?.get(0)}"
            vh.respuestatwo.text = "${list[position].respuestas?.get(1)}"
            vh.respuesta.setVisibility(View.VISIBLE)
            vh.respuestatwo.setVisibility(View.VISIBLE)
            vh.respuestathree.setVisibility(View.INVISIBLE)
            con == 0
        } else if (con == 1) {
            vh.respuesta.text = "${list[position].respuestas?.get(0)}"
            con == 0
            vh.respuesta.setVisibility(View.VISIBLE)
            vh.respuestatwo.setVisibility(View.VISIBLE)
            vh.respuestathree.setVisibility(View.VISIBLE)
        }

        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val preguntaA = vh.pregunta.text.toString()
        //val resp = vh.respuesta.text.toString()
        val correoV = mAuth.currentUser!!.email.toString()
        val pruebavCollection: CollectionReference
        pruebavCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
        val resultado = pruebavCollection.whereEqualTo("correo", correoV).whereEqualTo("id_pregunta", preguntaA)
        //beggin with consult
        resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                var respuestaBD = ""
                var documentId = ""
                var con = 0
                for (document in task.result!!) {
                    respuestaBD = document.get("respuesta").toString()
                    documentId = document.id
                }
                //I need validated the type of the voto
                if (respuestaBD.equals(vh.respuesta.text)) {
                    vh.respuesta.background
                    val color = Color.parseColor("#FA8258")
                    vh.respuesta.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                    // android:background="@drawable/btn_rounded_indigo_outline"
                } else if (respuestaBD.equals(vh.respuestatwo.text)) {
                    vh.respuestatwo.background
                    val color = Color.parseColor("#FA8258")
                    vh.respuestatwo.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))

                } else if (respuestaBD.equals(vh.respuestathree.text)) {
                    vh.respuestathree.background
                    val color = Color.parseColor("#FA8258")
                    vh.respuestathree.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
                }
            } else {
                Log.w("EXCEPTION", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool


        vh.respuesta.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                val pregunta = vh.pregunta.text.toString()
                val resp = vh.respuesta.text.toString()
                val correo = mAuth.currentUser!!.email.toString()
                val voto = Votacion()
                voto.id_pregunta = pregunta
                voto.id_empresa = id_empresa
                voto.respuesta = resp
                voto.correo = correo
                sentVoto(voto)
            }

            private fun sentVoto(voto: Votacion) {
                FirebaseApp.initializeApp(context)
                val userCollection: CollectionReference
                userCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
                val resultado = userCollection.whereEqualTo("correo", voto.correo).whereEqualTo("id_pregunta", voto.id_pregunta)
                //beggin with consult
                resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        var respuestaBD = ""
                        var documentId = ""
                        var con = 0
                        for (document in task.result!!) {
                            respuestaBD = document.get("respuesta").toString()
                            documentId = document.id
                            con++
                        }
                        //I need validated the type of the voto
                        if (con == 0) {
                            userCollection.add(voto).addOnSuccessListener {
                                showConfirmDialog()
                            }.addOnFailureListener {}
                        }
                        if (con == 1 && respuestaBD != voto.respuesta) {
                            //I need validated the type of the voto
                            userCollection.document(documentId).update("respuesta", voto.respuesta).addOnSuccessListener {
                                showConfirmDialogTwo()
                            }.addOnFailureListener { }
                        } else if (con == 1) {
                            Toast.makeText(context, "Ya se ha votado por esa opcion", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.w("EXCEPTION", "Error getting documents.", task.exception)
                    }
                })//end for expression lambdas this very cool
            }//end for hanlder

            private fun showConfirmDialog() {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_voto_postulado)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }

            private fun showConfirmDialogTwo() {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_voto_cambiado)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }
        })//first question
        vh.respuestatwo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                val pregunta = vh.pregunta.text.toString()
                val resp = vh.respuestatwo.text.toString()
                val correo = mAuth.currentUser!!.email.toString()
                val voto = Votacion()
                voto.id_pregunta = pregunta
                voto.respuesta = resp
                voto.id_empresa = id_empresa
                voto.correo = correo
                sentVoto(voto)
            }

            private fun sentVoto(voto: Votacion) {
                FirebaseApp.initializeApp(context)
                val userCollection: CollectionReference
                userCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
                val resultado = userCollection.whereEqualTo("correo", voto.correo).whereEqualTo("id_pregunta", voto.id_pregunta)
                //beggin with consult
                resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        var respuestaBD = ""
                        var documentId = ""
                        var con = 0
                        for (document in task.result!!) {
                            respuestaBD = document.get("respuesta").toString()
                            documentId = document.id
                            con++
                        }
                        //I need validated the type of the voto
                        if (con == 0) {
                            userCollection.add(voto).addOnSuccessListener {
                                showConfirmDialog()
                            }.addOnFailureListener {}
                        }
                        if (con == 1 && respuestaBD != voto.respuesta) {
                            userCollection.document(documentId).update("respuesta", voto.respuesta).addOnSuccessListener {
                                showConfirmDialogTwo()
                            }.addOnFailureListener { }
                        } else if (con == 1) {
                            Toast.makeText(context, "Ya se ha votado por esa opcion", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.w("EXCEPTION", "Error getting documents.", task.exception)
                    }
                })//end for expression lambdas this very cool
            }//end for hanlder

            private fun showConfirmDialog() {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_voto_postulado)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }

            private fun showConfirmDialogTwo() {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_voto_cambiado)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }

        })
        vh.respuestathree.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                val pregunta = vh.pregunta.text.toString()
                val resp = vh.respuestathree.text.toString()
                val correo = mAuth.currentUser!!.email.toString()
                val voto = Votacion()
                voto.id_pregunta = pregunta
                voto.id_empresa = id_empresa
                voto.respuesta = resp
                voto.correo = correo
                sentVoto(voto)
            }

            private fun sentVoto(voto: Votacion) {
                FirebaseApp.initializeApp(context)
                val userCollection: CollectionReference
                userCollection = FirebaseFirestore.getInstance().collection("pruebaVotaciones")
                val resultado = userCollection.whereEqualTo("correo", voto.correo).whereEqualTo("id_pregunta", voto.id_pregunta)
                //beggin with consult
                resultado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        var respuestaBD = ""
                        var documentId = ""
                        var con = 0
                        for (document in task.result!!) {
                            respuestaBD = document.get("respuesta").toString()
                            documentId = document.id
                            con++
                        }
                        //I need validated the type of the voto
                        if (con == 0) {
                            userCollection.add(voto).addOnSuccessListener {
                                showConfirmDialog()
                            }.addOnFailureListener {}
                        }

                        if (con == 1 && respuestaBD != voto.respuesta) {
                            userCollection.document(documentId).update("respuesta", voto.respuesta).addOnSuccessListener {
                                showConfirmDialogTwo()
                            }.addOnFailureListener { }
                        } else if (con == 1) {
                            Toast.makeText(context, "Ya se ha votado por esa opcion", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.w("EXCEPTION", "Error getting documents.", task.exception)
                    }
                })//end for expression lambdas this very cool
            }//end for hanlder

            private fun showConfirmDialog() {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_voto_postulado)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }

            private fun showConfirmDialogTwo() {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_voto_cambiado)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                (dialog.findViewById<View>(R.id.bt_close) as AppCompatButton).setOnClickListener { v ->
                    dialog.dismiss()
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }
        })

        return view
    }
}

class EncuestaViewHolder(view: View) {
    val pregunta: TextView = view.txtPregunta
    val respuesta: Button = view.txtRespuestas
    val respuestatwo: Button = view.txtRespuestas2
    val respuestathree: Button = view.txtRespuestas3
}