package com.material.components.model

class Actividades(var actividad: String = "",
                  var correo: String = "",
                  var estatus: String = "",
                  var id: String = "",
                  var id_usuario: String = "") {

    override fun toString() = "$actividad  $correo $estatus $id $id_usuario"
}