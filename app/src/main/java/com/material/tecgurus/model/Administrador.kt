package com.material.tecgurus.model
/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
class Administrador(private var correo: String = "",
                    private var id: String = "",
                    private var imagen: String = "",
                    private var contrasena: String = "",
                    private var token: String = "") {

    override fun toString() = "$correo  $id $imagen $token $contrasena "
}