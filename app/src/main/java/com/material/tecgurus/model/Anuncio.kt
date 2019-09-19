package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Anuncio(private var description: String = "",
private var titulo: String = "",
private var fecha: String = "",
private var ubicacion: String = "",
private var id: String = "",
private var id_empresa: String = "") {
 */
class Anuncio(var description: String = "",
              var titulo: String = "",
              var fecha: String = "",
              var ubicacion: String = "",
              var id: String = "",
              var id_empresa: String = "") {

    override fun toString() = "$description  $titulo $fecha $ubicacion $id $id_empresa"
}