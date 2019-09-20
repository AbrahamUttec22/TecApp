package com.material.tecgurus.model

import android.support.annotation.Keep

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class detalleAnuncios(
private var correo_usuario: String = "",
private var estatus: String = "",
private var id_empresa: String = "",
private var id: String = "",
private var id_anuncio: String = "") {
 */
@Keep
class detalleAnuncios(
         var correo_usuario: String = "",
         var estatus: String = "",
         var id_empresa: String = "",
         var id: String = "",
         var id_anuncio: String = "") {
    override fun toString() = "$correo_usuario  $estatus $id_empresa $id_anuncio $id"
}