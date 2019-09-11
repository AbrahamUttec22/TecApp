package com.material.tecgurus.model

class Actividades(var actividad: String = "",
                  var descripcion: String = "",
                  var correo: String = "",
                  var estatus: String = "",
                  var id: String = "",
                  var id_usuario: String = "",
                  var id_empresa: String = "",
                  var fecha_compromiso: String = "",
                  var email_asigno: String = ""

        // var fecha_hora_asignada: String = "",
        //var fecha_hora_terminada: String = ""
) {

    override fun toString() = "$actividad  $correo $estatus $id $id_usuario $id_empresa $fecha_compromiso $descripcion"
}