package com.material.components.model


class detalleEncuestas(
        var correo_usuario: String = "",
        var estatus: String = "",
        var id_empresa: String = "",
        var id_anuncio: String = "") {
    override fun toString() = "$correo_usuario  $estatus $id_empresa $id_anuncio"
}