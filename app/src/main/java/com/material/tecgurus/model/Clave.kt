package com.material.tecgurus.model

class Clave(var acceso: String = "",var id_empresa: String = "") {
    override fun toString() = "$acceso $id_empresa"
}