package com.material.components.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.AppCompatRadioButton
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
import com.material.components.model.Actividades
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
        //get data the user
        var email = "${list[position].email}"//email
        var ubicacion = "${list[position].ubicacion}"//ubicacion
        val fullName = "${list[position].name}"//nombre
        vh.fullName.text = fullName
        var rol = "${list[position].rol}"//rol
        var direccion = "${list[position].direccion}"//direccion
        var edad = "${list[position].edad}"//edad
        var telefono = "${list[position].telefono}"//telefono
        val id = "${list[position].id}"
        var id_empresa = "${list[position].id_empresa}"//id_empresa
        Glide
                .with(this.context)
                .load("${list[position].ubicacion}")
                .into(view.imageUser)

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
        vh.actualizar.setOnClickListener(object : View.OnClickListener {
            var calendario = Calendar.getInstance()
            override fun onClick(position: View?) {
                var usuario = Usuario()
                usuario.email = email
                usuario.ubicacion = ubicacion
                usuario.name = fullName
                usuario.rol = rol
                usuario.direccion = direccion
                usuario.edad = edad
                usuario.telefono = telefono
                usuario.id = id
                usuario.id_empresa = id
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
                if (usuario.rol.equals("administrador")) {
                    (dialog.findViewById<View>(R.id.ac_radio_adminitrador) as AppCompatRadioButton).isChecked = true
                    (dialog.findViewById<View>(R.id.ac_radio_usuario) as AppCompatRadioButton).isChecked = false
                } else {
                    (dialog.findViewById<View>(R.id.ac_radio_adminitrador) as AppCompatRadioButton).isChecked = false
                    (dialog.findViewById<View>(R.id.ac_radio_usuario) as AppCompatRadioButton).isChecked = true
                }

                (dialog.findViewById<View>(R.id.btnActualizarUsuario) as Button).setOnClickListener {
                    //after that I get the data
                    if ((dialog.findViewById<View>(R.id.ac_radio_adminitrador) as AppCompatRadioButton).isChecked)
                        usuario.rol = "administrador"
                    if ((dialog.findViewById<View>(R.id.ac_radio_usuario) as AppCompatRadioButton).isChecked)
                        usuario.rol = "usuario"
                    //first save the user on authe
                    updateUsuario(usuario)
                    dialog.dismiss()
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
                    Toast.makeText(context, "El rol se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el usuario intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder

        })
        vh.actividad.setOnClickListener(object : View.OnClickListener {
            var calendario = Calendar.getInstance()
            override fun onClick(position: View?) {
                var usuario = Usuario()
                usuario.email = email
                usuario.ubicacion = ubicacion
                usuario.name = fullName
                usuario.rol = rol
                usuario.direccion = direccion
                usuario.edad = edad
                usuario.telefono = telefono
                usuario.id = id
                usuario.id_empresa = id
                showDialog(usuario)
            }

            private fun showDialog(usuario: Usuario) {
                //the header from dialog
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
                dialog.setContentView(R.layout.dialog_actividad_usuario)
                dialog.setCancelable(true)
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(dialog.window!!.attributes)
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                //in this code I get the information on cloud firestore
                var txt1 = (dialog.findViewById<View>(R.id.txtDescriptionActividad) as EditText)
                (dialog.findViewById<View>(R.id.btnSaveActividad) as Button).setOnClickListener {
                    var dactividad = txt1.text.toString()
                    if (!dactividad.isNullOrEmpty()) {
                       var actividad = Actividades()
                        actividad.actividad = dactividad//get the field for the view
                        actividad.correo = usuario.email
                        actividad.estatus = "pendiente"
                        actividad.id = ""
                        actividad.id_usuario = usuario.id
                        //first save the user on authe
                        saveActividad(actividad)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Completa los campos", Toast.LENGTH_LONG).show()
                    }
                }
                dialog.show()
                dialog.window!!.attributes = lp
            }
            private fun saveActividad(actividad: Actividades) {
                FirebaseApp.initializeApp(context)
                val actividadCollection: CollectionReference
                actividadCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //save the activity
                actividadCollection.add(actividad).addOnSuccessListener {
                    actividadCollection.document(it.id).update("id", it.id).addOnSuccessListener {}.addOnFailureListener { }
                    Toast.makeText(context, "Se ha asignado correctamente la actividad", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error asignando la actividad", Toast.LENGTH_LONG).show()
                }
            }//end for hanlder
        })

        return view
    }
}

private class UserViewHolder(view: View) {
    val fullName: TextView = view.textViewName
    val actualizar: Button = view.btnActualizarUsuario
    val eliminar: Button = view.btnEliminarUsuario
    val actividad: Button = view.btnAsignarActividad
}