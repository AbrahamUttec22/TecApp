package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Clave(private var acceso: String = "",
private var id_empresa: String = "") {
 */
class Clave(var acceso: String = "",
            var id_empresa: String = "") {
    override fun toString() = "$acceso $id_empresa"
}