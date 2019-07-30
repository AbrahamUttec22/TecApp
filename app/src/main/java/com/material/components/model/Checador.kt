package com.material.components.model

class Checador(var fecha: String = "",
               var hora: String = "",
               var id: String = "",
               var id_empresa: String = "",
               var id_usuario: String = "",
               var nombre: String = "") {

    override fun toString() = "$fecha  $hora  $id_empresa $id_usuario $id"
}