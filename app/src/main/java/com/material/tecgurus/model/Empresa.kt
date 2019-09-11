package com.material.tecgurus.model

/**
 * @author Abraham
 */
class Empresa(var nombre: String = "",
              var correo: String = "",
              var telefono: String = "",
              var direccion: String = "",//direccion,edad,telefono campos nuevos
              var foto: String = "",
              var giro: String = "",
              var id_empresa: String = "",
              var estatus: String = "",
              var token: String = "",
              var fecha_vencimiento_plan: String = "",
              var fecha_registro: String = "",
              var uid: String = "") {

    override fun toString() = "$nombre $fecha_vencimiento_plan $correo $telefono $direccion" +
            " $foto $giro $id_empresa $token $fecha_registro $estatus"
}