package com.material.tecgurus.model
/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
class Encuesta(private var pregunta: String = "",
               private var respuestas: List<String>? = null,
               private var status: String = "",
               private var id_empresa: String = "") {
    override fun toString() = "$pregunta  $respuestas $status $id_empresa"
}