package com.material.tecgurus.model

class Pagos(var id: String = "",
            var fecha_pago: String = "",
            var id_empresa: String = "",
            var hora_pago: String = "",
            var estatus: String = "",
            var id_pago_paypal: String = "",
            var monto: String = "") {

    override fun toString() = "$id  $id_pago_paypal $fecha_pago $id_empresa $hora_pago $monto $estatus "
}