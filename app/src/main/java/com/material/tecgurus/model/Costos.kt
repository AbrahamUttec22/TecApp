package com.material.tecgurus.model

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Costos(private var id: String = "",
private var plan: String = "",
private var tipo_plan: String = "",
private var costo: String = "") {
 */
class Costos(var id: String = "",
             var plan: String = "",
             var tipo_plan: String = "",
             var costo: String = "") {

    override fun toString() = "$id  $plan $costo $tipo_plan "
}