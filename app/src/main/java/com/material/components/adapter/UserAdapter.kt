package com.material.components.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.material.components.R
import com.material.components.model.Evento
import com.material.components.model.Usuario
import kotlinx.android.synthetic.main.list_view_administrar_eveto.view.*
import kotlinx.android.synthetic.main.list_view_usuario.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham
 */
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
        val id = "${list[position].id}"
        vh.eliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val usuario = Usuario()
                usuario.id = id
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    deleteUsuario(usuario)
                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        .show()

            }
            private fun deleteUsuario(usuario: Usuario) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Usuarios")
                //only this source I update the status,
                eventoCollection.document(usuario.id).delete().addOnSuccessListener {
                    Toast.makeText(context, "El usuario se ha eliminado correctamente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  elimando al usuario intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })
        var usuario = Usuario()
        usuario.rol = "${list[position].id}"

      /*  vh.actualizar.setOnClickListener(object : View.OnClickListener {
            var calendario = Calendar.getInstance()
            override fun onClick(position: View?) {
                usuario.id = id
                showDialog(usuario)
            }
            private fun showDialog(usuario: Usuario) {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_actualizar_usuario)
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


                (dialog.findViewById<View>(R.id.btnActualizarEvento2) as Button).setOnClickListener {
                    //after that I get the data
                    var tituloNuevo = txt1.text.toString()
                    var descriptionNuevo = txt2.text.toString()
                    var fechaNuevo = txt3.text.toString()
                    if (!tituloNuevo.isNullOrEmpty() && !descriptionNuevo.isNullOrEmpty() && !fechaNuevo.isNullOrEmpty()) {
                        eveto.titulo=tituloNuevo
                        eveto.description=descriptionNuevo
                        eveto.fecha=fechaNuevo
                        updateUsuario(eveto)
                        dialog.dismiss()
                        Toast.makeText(context, "El usuario se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }
            private fun updateUsuario(usuario: Usuario) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Usuarios")
                //only this source I update the status,
                eventoCollection.document(usuario.id).update("rol", usuario.rol).addOnSuccessListener {
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el usuario intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder

        })
       */ return view
    }
}

private class UserViewHolder(view: View) {
    val fullName: TextView = view.textViewName
    val actualizar: Button = view.btnActualizarUsuario
    val eliminar: Button = view.btnEliminarUsuario
}