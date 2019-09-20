package com.material.tecgurus.adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.AppCompatRadioButton
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.alejandrolora.finalapp.inflate
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.tecgurus.R
import com.material.tecgurus.model.Actividades
import com.material.tecgurus.model.Usuario
import kotlinx.android.synthetic.main.list_view_usuario.view.*
import java.text.SimpleDateFormat
import java.util.*

import com.material.tecgurus.message.ApiClient
import com.material.tecgurus.message.ApiInter
import com.material.tecgurus.message.Notification
import com.material.tecgurus.message.RequestNotificaton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import java.lang.Exception

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

        vh.Correo.text = email

        //val uid = "${list[position].uid}"//uid
        Glide
                .with(this.context)
                .load("${list[position].ubicacion}")
                .into(view.imageUser)
        vh.EmailUser.setVisibility(View.INVISIBLE)
        //vh.eliminar.setVisibility(View.INVISIBLE)
        vh.EmailUser.text = id

        var nombre_asigno = ""
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var email_asigno = mAuth.currentUser!!.email.toString()
        val userCollection: CollectionReference
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        val empleado = userCollection.whereEqualTo("email", email_asigno).whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    nombre_asigno = document.get("name").toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool


        val empresaCollection: CollectionReference
        empresaCollection = FirebaseFirestore.getInstance().collection("Empresas")
        val empresa = empresaCollection.whereEqualTo("correo", email_asigno).whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empresa.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    nombre_asigno = document.get("nombre").toString()
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool


        /* vh.eliminar.setOnClickListener(object : View.OnClickListener {
             override fun onClick(position: View?) {
                 val usuario = Usuario()
                 usuario.id = id
                 usuario.uid = uid
                 usuario.email = email
                 val builder = AlertDialog.Builder(context)
                 builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                     deleteUsuario(usuario)
                 }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() }).show()
             }

             private fun deleteUsuario(usuario: Usuario) {
                 FirebaseApp.initializeApp(context)
                 val eventoCollection: CollectionReference
                 eventoCollection = FirebaseFirestore.getInstance().collection("Actividades")
                 //only this source I update the status,
                 eventoCollection.document(usuario.id).delete().addOnSuccessListener {
                     Toast.makeText(context, "El usuario se ha eliminado correctamente", Toast.LENGTH_LONG).show()
                 }.addOnFailureListener { Toast.makeText(context, "Error  elimando al usuario intenta de nuevo", Toast.LENGTH_LONG).show() }

                 //var mAuth: FirebaseAuth = FirebaseAuth.getInstance().currentUser.delete(uid)
             }//end for hanlder
         })*/

        try {
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
                    usuario.id_empresa = id_empresa
                    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                    var email = mAuth.currentUser!!.email.toString()

                    showDialog(usuario, email)
                }

                private fun showDialog(usuario: Usuario, email_asigno: String) {
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
                    var txt2 = (dialog.findViewById<View>(R.id.txtDescriptionActivida) as EditText)
                    var txt3 = (dialog.findViewById<View>(R.id.txtFechaActividad) as EditText)

                    //see views front end
                    var date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // TODO Auto-generated method stub
                        calendario.set(Calendar.YEAR, year)
                        calendario.set(Calendar.MONTH, monthOfYear)
                        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val formatoDeFecha = "dd/MM/yy" //In which you need put here
                        val sdf = SimpleDateFormat(formatoDeFecha, Locale.US)
                        txt3.setText(sdf.format(calendario.time))
                    }

                    txt3.setOnClickListener {
                        var datee = DatePickerDialog(context, date, calendario
                                .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                                calendario.get(Calendar.DAY_OF_MONTH))
                        datee.datePicker.minDate = System.currentTimeMillis()
                        datee.show()
                    }

                    (dialog.findViewById<View>(R.id.btnSaveActividad) as Button).setOnClickListener {
                        var dactividad = txt1.text.toString()
                        var ddescripcion = txt2.text.toString()
                        var dfecha = txt3.text.toString()

                        if (!dactividad.isNullOrEmpty() && !ddescripcion.isNullOrEmpty() && !dfecha.isNullOrEmpty()) {
                            var actividad = Actividades()
                            actividad.actividad = dactividad//get the field for the view
                            actividad.correo = usuario.email
                            actividad.estatus = "actividades"
                            actividad.id_usuario = usuario.id
                            actividad.id_empresa = usuario.id_empresa
                            actividad.email_asigno = email_asigno//parametro del frontend
                            actividad.descripcion = ddescripcion//parametro del frontend
                            actividad.fecha_compromiso = dfecha//parametro del frontend
                            actividad.id = ""

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
                    try {
                        FirebaseApp.initializeApp(context)
                        val actividadCollection: CollectionReference
                        val userCollection: CollectionReference
                        actividadCollection = FirebaseFirestore.getInstance().collection("Actividades")
                        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
                        //save the activity
                        actividadCollection.add(actividad).addOnSuccessListener {
                            actividadCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                            }.addOnFailureListener { }
                            val empleado = userCollection.whereEqualTo("id", actividad.id_usuario)
                            //beggin with consult
                            empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val token = document.get("token").toString()
                                        sendNotificationToPatner(token)
                                    }
                                } else {
                                    Log.w("saasas", "Error getting documents.", task.exception)
                                }
                            })//end for expression lambdas this very cool

                            Toast.makeText(context, "Se ha asignado correctamente la actividad", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Error asignando la actividad", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {

                    }

                }//end for hanlder

                private fun sendNotificationToPatner(token: String) {
                    var bo=nombre_asigno + " te asigno una actividad"
                    val notification = Notification()
                    notification.body=bo
                    notification.title="Actividad"
                    Log.w("IGOT", "body" + notification)
                    val requestNotificaton = RequestNotificaton()
                    //token is id , whom you want to send notification ,
                    requestNotificaton.token = token
                    requestNotificaton.notification = notification
                    Log.w("IGOT", "token" + token)
                    val apiService = ApiClient.getClient().create(ApiInter::class.java)
                    val responseBodyCall = apiService.sendChatNotification(requestNotificaton)
                    Log.w("IGOT", "alfo"+responseBodyCall.toString())
                    responseBodyCall.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            Log.w("IGOT", "SI LA ENVIE"+call.toString()+response.toString())
                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.w("IGOT", "NO LA ENVIE")

                        }
                    })
                }
            })

            vh.eliminar.setOnClickListener(object : View.OnClickListener {
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

                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                        updateUsuario(usuario)
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                            .show()
                }

                private fun updateUsuario(usuario: Usuario) {
                    FirebaseApp.initializeApp(context)
                    val eventoCollection: CollectionReference
                    eventoCollection = FirebaseFirestore.getInstance().collection("Usuarios")
                    //only this source I update the status,
                    //0 false 1 true
                    eventoCollection.document(usuario.id).update("estatus", "0").addOnSuccessListener {
                        Toast.makeText(context, "El rol se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el usuario intenta de nuevo", Toast.LENGTH_LONG).show() }
                }//end for hanlder

            })

            vh.actualizar2.setOnClickListener(object : View.OnClickListener {
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

            vh.actividad2.setOnClickListener(object : View.OnClickListener {
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
                    usuario.id_empresa = id_empresa
                    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                    var email = mAuth.currentUser!!.email.toString()

                    showDialog(usuario, email)
                }

                private fun showDialog(usuario: Usuario, email_asigno: String) {
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
                    var txt2 = (dialog.findViewById<View>(R.id.txtDescriptionActivida) as EditText)
                    var txt3 = (dialog.findViewById<View>(R.id.txtFechaActividad) as EditText)

                    //see views front end
                    var date: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // TODO Auto-generated method stub
                        calendario.set(Calendar.YEAR, year)
                        calendario.set(Calendar.MONTH, monthOfYear)
                        calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val formatoDeFecha = "dd/MM/yy" //In which you need put here
                        val sdf = SimpleDateFormat(formatoDeFecha, Locale.US)
                        txt3.setText(sdf.format(calendario.time))
                    }

                    txt3.setOnClickListener {
                        var datee = DatePickerDialog(context, date, calendario
                                .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                                calendario.get(Calendar.DAY_OF_MONTH))
                        datee.datePicker.minDate = System.currentTimeMillis()
                        datee.show()
                    }

                    (dialog.findViewById<View>(R.id.btnSaveActividad) as Button).setOnClickListener {
                        var dactividad = txt1.text.toString()
                        var ddescripcion = txt2.text.toString()
                        var dfecha = txt3.text.toString()

                        if (!dactividad.isNullOrEmpty() && !ddescripcion.isNullOrEmpty() && !dfecha.isNullOrEmpty()) {
                            var actividad = Actividades()
                            actividad.actividad = dactividad//get the field for the view
                            actividad.correo = usuario.email
                            actividad.estatus = "actividades"
                            actividad.id_usuario = usuario.id
                            actividad.id_empresa = usuario.id_empresa
                            actividad.email_asigno = email_asigno//parametro del frontend
                            actividad.descripcion = ddescripcion//parametro del frontend
                            actividad.fecha_compromiso = dfecha//parametro del frontend
                            actividad.id = ""

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
                    try {
                        FirebaseApp.initializeApp(context)
                        val actividadCollection: CollectionReference
                        val userCollection: CollectionReference
                        actividadCollection = FirebaseFirestore.getInstance().collection("Actividades")
                        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
                        //save the activity
                        actividadCollection.add(actividad).addOnSuccessListener {
                            actividadCollection.document(it.id).update("id", it.id).addOnSuccessListener {
                            }.addOnFailureListener { }
                            val empleado = userCollection.whereEqualTo("id", actividad.id_usuario)
                            //beggin with consult
                            empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val token = document.get("token").toString()
                                        sendNotificationToPatner(token)
                                    }
                                } else {
                                    Log.w("saasas", "Error getting documents.", task.exception)
                                }
                            })//end for expression lambdas this very cool

                            Toast.makeText(context, "Se ha asignado correctamente la actividad", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Error asignando la actividad", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {

                    }

                }//end for hanlder

                private fun sendNotificationToPatner(token: String) {
                    var bo=nombre_asigno + " te asigno una actividad"
                    val notification = Notification()
                    notification.body=bo
                    notification.title="Actividad"
                    Log.w("IGOT", "body" + notification)
                    val requestNotificaton = RequestNotificaton()
                    Log.w("IGOT", "haciendo el request" + nombre_asigno)
                    //token is id , whom you want to send notification ,
                    requestNotificaton.token = token
                    requestNotificaton.notification = notification
                    Log.w("IGOT", "token" + token)
                    val apiService = ApiClient.getClient().create(ApiInter::class.java)
                    val responseBodyCall = apiService.sendChatNotification(requestNotificaton)
                    Log.w("IGOT", "token" + token)
                    responseBodyCall.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            Log.w("IGOT", "SI LA ENVIE"+call.toString()+response.toString())
                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()


                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.w("IGOT", "NO LA ENVIE")

                        }
                    })
                }


            })

            vh.eliminar2.setOnClickListener(object : View.OnClickListener {
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

                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                        updateUsuario(usuario)
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                            .show()
                }

                private fun updateUsuario(usuario: Usuario) {
                    FirebaseApp.initializeApp(context)
                    val eventoCollection: CollectionReference
                    eventoCollection = FirebaseFirestore.getInstance().collection("Usuarios")
                    //only this source I update the status,
                    //0 false 1 true
                    eventoCollection.document(usuario.id).update("estatus", "0").addOnSuccessListener {
                        Toast.makeText(context, "El rol se ha actualizado correctamente", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el usuario intenta de nuevo", Toast.LENGTH_LONG).show() }
                }//end for hanlder

            })

        } catch (e: java.lang.Exception) {
        }
        return view
    }
}

private class UserViewHolder(view: View) {
    val fullName: TextView = view.textViewName
    val actualizar: ImageButton = view.btnActualizarUsuario
    val actualizar2: TextView = view.btnActualizarUsuariot
    val actividad: ImageButton = view.btnAsignarActividad
    val actividad2: TextView = view.btnAsignarActividadt
    val EmailUser: TextView = view.textemail
    val Correo: TextView = view.textemailtwo
    val eliminar: ImageButton = view.btnEliminarUsuario
    val eliminar2: TextView = view.btnEliminarUsuariot
}