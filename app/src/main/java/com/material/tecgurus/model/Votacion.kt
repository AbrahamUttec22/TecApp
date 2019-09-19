package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
class Votacion(private var id_pregunta: String = "",
               private var respuesta: String = "",
               private var correo: String = "",
               private var id_empresa: String = "") {
    override fun toString() = "$id_pregunta  $respuesta  $correo $id_empresa"
}