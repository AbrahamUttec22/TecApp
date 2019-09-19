package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Usuario(private var email: String = "",
private var ubicacion: String = "",
private var name: String = "",
private var rol: String = "",//direccion,edad,telefono campos nuevos
private var direccion: String = "",
private var edad: String = "",
private var telefono: String = "",
private var id: String = "",
private var id_empresa: String = "",
private var token: String = "",
private var estatus: String = "",
private var uid: String = "") {

 */
class Usuario(var email: String = "",
              var ubicacion: String = "",
              var name: String = "",
              var rol: String = "",//direccion,edad,telefono campos nuevos
              var direccion: String = "",
              var edad: String = "",
              var telefono: String = "",
              var id: String = "",
              var id_empresa: String = "",
              var token: String = "",
              var estatus: String = "",
              var uid: String = "") {

    override fun toString() = "$uid $email  $name $ubicacion $rol $direccion $edad $telefono $id $id_empresa $token $estatus"
}