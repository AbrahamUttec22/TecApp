package com.material.components.model

class detalleAnuncios(
        var correo_usuario: String = "",
        var estatus: String = "",
        var id_empresa: String = "",
        var id: String = "",
        var id_anuncio: String = "") {
    override fun toString() = "$correo_usuario  $estatus $id_empresa $id_anuncio $id"
}