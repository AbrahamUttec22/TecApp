package com.material.tecgurus.model

import android.support.annotation.Keep

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Clave(private var acceso: String = "",
private var id_empresa: String = "") {
 */
@Keep
class Clave(var acceso: String = "",
            var id_empresa: String = "") {
    override fun toString() = "$acceso $id_empresa"
}