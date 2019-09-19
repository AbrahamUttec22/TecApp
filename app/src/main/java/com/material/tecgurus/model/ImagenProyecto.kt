package com.material.tecgurus.model
/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
class ImagenProyecto(private var name: String = "",
                     private var ubicacion: String = "",
                     private var id_empresa: String = "") {

    override fun toString() = "$name $ubicacion $id_empresa"
}