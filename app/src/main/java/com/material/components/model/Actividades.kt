package com.material.components.model

class Actividades(var actividad: String = "",
                  var correo: String = "",
                  var estatus: String = "",
                  var id: String = "",
                  var id_usuario: String = "",
                  var fecha_hora_asignada: String = "",
                  var fecha_hora_terminada: String = ""
) {

    override fun toString() = "$actividad  $correo $estatus $id $id_usuario $fecha_hora_asignada $fecha_hora_terminada"
}