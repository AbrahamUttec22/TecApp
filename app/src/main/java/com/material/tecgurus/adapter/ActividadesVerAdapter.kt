package com.material.tecgurus.adapter

/**
 * @author Abraham Casas Aguilar
 */
/*
class ActividadesVerAdapter(val context: Context, val layout: Int, val list: List<Actividades>) : BaseAdapter() {

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
        val vh: ActividadesVerViewHolder
        if (convertView == null) {
            view = parent!!.inflate(layout)
            vh = ActividadesVerViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ActividadesVerViewHolder
        }
        val status = "${list[position].estatus}"
        val actividad = "${list[position].actividad}"
        val asignada= "${list[position].fecha_hora_asignada}"
        val terminada="${list[position].fecha_hora_terminada}"

        if (status.equals("pendiente")) {
            vh.simpleSwitch.isChecked = false
            vh.simpleSwitch.isClickable = false
            vh.texto.text = "Pendiente"
            vh.actividad.text=actividad
            //val color = Color.parseColor("#999999")
            //vh.eliminar.getBackground().mutate().setColorFilter(PorterDuffColorFilter(color, PorterDuff.Mode.SRC))
            vh.eliminar.setVisibility(View.INVISIBLE)
            vh.fecha_estatus.text="Asignada: "+asignada
        } else if (status.equals("realizado")) {
            vh.actividad.text=actividad
            vh.simpleSwitch.isChecked = true
            vh.simpleSwitch.isClickable = false
            vh.texto.text = "Realizada"
            vh.eliminar.setVisibility(View.VISIBLE)
            vh.fecha_estatus.text="Asignada: "+asignada+" \n Concluida: "+terminada
        }
        val id = "${list[position].id}"

        vh.eliminar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(position: View?) {
                val actividad = Actividades()
                actividad.id = id
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Estas seguro de eliminar?").setPositiveButton("Si", DialogInterface.OnClickListener { dialog, id ->
                    sentVoto(actividad)
                }).setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
                        .show()
            }

            private fun sentVoto(actividad: Actividades) {
                FirebaseApp.initializeApp(context)
                val eventoCollection: CollectionReference
                eventoCollection = FirebaseFirestore.getInstance().collection("Actividades")
                //only this source I update the status,
                eventoCollection.document(actividad.id).delete().addOnSuccessListener {
                    Toast.makeText(context, "Se ha eliminado correctamente la actividad", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { Toast.makeText(context, "Error  elimando la actividad intenta de nuevo", Toast.LENGTH_LONG).show() }
            }//end for hanlder
        })
        return view
    }

}

class ActividadesVerViewHolder(view: View) {
    val actividad: TextView = view.txtActividadDialog
    val simpleSwitch: Switch = view.simpleSwitchDialog
    val texto: TextView = view.txtCambiarDialog
    val eliminar: Button = view.btnEliminarActividadver
    val fecha_estatus: TextView = view.txtFechaVer
}


*/
