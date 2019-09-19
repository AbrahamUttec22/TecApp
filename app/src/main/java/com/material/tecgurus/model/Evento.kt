package com.material.tecgurus.model
/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
class Evento(private var description: String = "",
             private var fecha: String = "",
             private  var image: String = "",
             private  var titulo: String = "",
             private   var ubicacion: String = "",
             private   var id: String = "",
             private   var id_empresa: String = "",
             private   var hora: String = "") {
    override fun toString() = "$description  $fecha $image $titulo $ubicacion $id $id_empresa $hora"
}