package com.material.tecgurus.model
/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
class Checador(private var fecha: String = "",
               private var hora: String = "",
               private var id: String = "",
               private var id_empresa: String = "",
               private var id_usuario: String = "",
               private var nombre: String = "") {

    override fun toString() = "$fecha  $hora  $id_empresa $id_usuario $id"
}