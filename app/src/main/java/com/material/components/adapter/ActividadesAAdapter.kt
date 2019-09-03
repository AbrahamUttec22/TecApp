package com.material.components.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.alejandrolora.finalapp.inflate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.material.components.model.Actividades
import kotlinx.android.synthetic.main.list_view_actividades.view.*
import kotlinx.android.synthetic.main.list_view_actividades_admin.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Abraham Casas Aguilar
 */
class ActividadesAAdapter(val context: Context?, val layout: Int, val list: List<Actividades>) : BaseAdapter() {

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
        val vh: ActividadesViewHolderAdmin
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesViewHolderAdmin(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesViewHolderAdmin
        }

        val status = "${list[position].estatus}"//no mostrar
        val id = "${list[position].id}"//no mostrar id del documento
        var correo = "${list[position].descripcion}"//no mostrar
        var id_usuario = "${list[position].id_usuario}"//no mostrar
        var id_empresa = "${list[position].id_empresa}"//no mostrar
        var email_asigno = "${list[position].email_asigno}"//Actividad Asignada por
        var correo_asignado = "${list[position].correo}"//Actividad Asignada por


        var titulo = "${list[position].actividad}"//mostrar
        var descripcion = "${list[position].descripcion}"//mostrar
        var fecha_compromiso = "${list[position].fecha_compromiso}"//mostrar

        vh.actividad.text = titulo
        vh.descripcion.text = descripcion
        vh.fechaac.text = "Fecha Compromiso: " + fecha_compromiso


        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd/MM/yy")
        val fechaCalendar = df.format(c.getTime()).toString()

        if (fechaCalendar.equals(fecha_compromiso)) {
            vh.estatusFecha.text = "Hoy  vence esta actividad"
        } else {
            var diaCompromiso = fecha_compromiso.substring(0, 2).toInt()//dd
            var mesCompromiso = fecha_compromiso.substring(3, 5).toInt()//mm
            var anoCompromiso = fecha_compromiso.substring(6, 8).toInt()//yyyy

            var diaCalendar = fechaCalendar.substring(0, 2).toInt()//dd
            var mesCalendar = fechaCalendar.substring(3, 5).toInt()//mm
            var anoCalendar = fechaCalendar.substring(6, 8).toInt()//yyyy

            if (mesCompromiso == mesCalendar) {
                var resta = diaCompromiso - diaCalendar
                if (resta == 1) {
                    vh.estatusFecha.text = "En " + resta + " día vence esta actividad"
                } else if (resta > 1) {
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (resta < 0) {
                    vh.estatusFecha.text = "La actividad ya venció,  considera pasarla a finalizada"
                }
            } else {
                var restameses = mesCompromiso - mesCalendar
                if (restameses == 1) {
                    var resta = (diaCompromiso - diaCalendar) + 30
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 2) {
                    var resta = (diaCompromiso - diaCalendar) + 60
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 3) {
                    var resta = (diaCompromiso - diaCalendar) + 90
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 4) {
                    var resta = (diaCompromiso - diaCalendar) + 120
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 5) {
                    var resta = (diaCompromiso - diaCalendar) + 150
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 6) {
                    var resta = (diaCompromiso - diaCalendar) + 180
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 7) {
                    var resta = (diaCompromiso - diaCalendar) + 210
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 8) {
                    var resta = (diaCompromiso - diaCalendar) + 240
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 9) {
                    var resta = (diaCompromiso - diaCalendar) + 270
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 10) {
                    var resta = (diaCompromiso - diaCalendar) + 300
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                } else if (restameses == 11) {
                    var resta = (diaCompromiso - diaCalendar) + 330
                    vh.estatusFecha.text = "En " + resta + " días vence esta actividad"
                }
            }
        }

        val userCollection: CollectionReference
        userCollection = FirebaseFirestore.getInstance().collection("Usuarios")
        val empleado = userCollection.whereEqualTo("email", correo_asignado).whereEqualTo("id_empresa", id_empresa)
        //beggin with consult
        empleado.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    vh.info.text = "Le has asignado esta actividad a: " + document.get("name")
                }
            } else {
                Log.w("saasas", "Error getting documents.", task.exception)
            }
        })//end for expression lambdas this very cool




        vh.mover.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                var activid = Actividades()
                activid.id = id
                updateActividad(activid)
            }

            private fun updateActividad(actividad: Actividades) {
                if (context != null) {
                    FirebaseApp.initializeApp(context)
                }
                val actividadesCollection: CollectionReference
                actividadesCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //only this source I update the status,
                actividadesCollection.document(actividad.id).update("estatus", "proceso").addOnSuccessListener {
                    Toast.makeText(context, "Se ha movido la actividad a: En Proceso", Toast.LENGTH_LONG).show()

                }.addOnFailureListener { Toast.makeText(context, "Error  actualizando el evento intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })
        return view
    }


}

class ActividadesViewHolderAdmin(view: View) {
    val actividad: TextView = view.txtActividadAdmin
    val descripcion: TextView = view.txtDescripcionAcAdmin
    val info: TextView = view.txInfoAcAdmin
    val fechaac: TextView = view.txtFechaActiviAdmin
    val mover: Button = view.moverprocesoAdmin
    val estatusFecha: TextView = view.txtEstatusFechaAdmin

}