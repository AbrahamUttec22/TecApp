package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Empresa(private var nombre: String = "",
private var correo: String = "",
private var telefono: String = "",
private var direccion: String = "",//direccion,edad,telefono campos nuevos
private var foto: String = "",
private var giro: String = "",
private var id_empresa: String = "",
private var estatus: String = "",
private var token: String = "",
private var fecha_vencimiento_plan: String = "",
private var fecha_registro: String = "",
private var uid: String = "") {

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
            " $foto $giro $id_empresa $token $fecha_registro $estatus $uid"
}