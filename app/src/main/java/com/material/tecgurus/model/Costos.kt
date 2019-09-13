package com.material.tecgurus.model

class Costos(var id: String = "",
             var plan: String = "",
             var tipo_plan: String = "",
             var costo: String = "") {

    override fun toString() = "$id  $plan $costo $tipo_plan "
}