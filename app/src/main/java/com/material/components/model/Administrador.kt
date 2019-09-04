package com.material.components.model

class Administrador(var correo: String = "",
                    var id: String = "",
                    var imagen: String = "",
                    var contrasena: String = "",
                    var token: String = "") {

    override fun toString() = "$correo  $id $imagen $token $contrasena "
}