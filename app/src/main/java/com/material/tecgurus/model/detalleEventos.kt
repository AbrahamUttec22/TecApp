package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas
 *    private var correo_usuario: String = "",
private var estatus: String = "",
private var id_empresa: String = "",
private var id: String = "",
private var id_evento: String = "") {
override fun toString() = "$correo_usuario  $es
 */
class detalleEventos(
        var correo_usuario: String = "",
        var estatus: String = "",
        var id_empresa: String = "",
        var id: String = "",
        var id_evento: String = "") {
    override fun toString() = "$correo_usuario  $estatus $id_empresa $id_evento $id"
}