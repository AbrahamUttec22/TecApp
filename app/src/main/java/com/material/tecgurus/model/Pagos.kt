package com.material.tecgurus.model

import android.support.annotation.Keep

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Pagos(private var id: String = "",
private var fecha_pago: String = "",
private var id_empresa: String = "",
private var hora_pago: String = "",
private var estatus: String = "",
private var id_pago_paypal: String = "",
private var monto: String = "") {
 */
@Keep
class Pagos(var id: String = "",
            var fecha_pago: String = "",
            var id_empresa: String = "",
            var hora_pago: String = "",
            var estatus: String = "",
            var id_pago_paypal: String = "",
            var monto: String = "") {

    override fun toString() = "$id  $id_pago_paypal $fecha_pago $id_empresa $hora_pago $monto $estatus "
}