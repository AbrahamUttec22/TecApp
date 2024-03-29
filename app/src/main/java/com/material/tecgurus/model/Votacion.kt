package com.material.tecgurus.model

import android.support.annotation.Keep

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 */
@Keep
class Votacion(var id_pregunta: String = "",
               var respuesta: String = "",
               var correo: String = "",
               var id_empresa: String = "") {
    override fun toString() = "$id_pregunta  $respuesta  $correo $id_empresa"
}