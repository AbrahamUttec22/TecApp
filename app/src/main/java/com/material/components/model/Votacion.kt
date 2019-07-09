package com.material.components.model

class Votacion(var id_pregunta: String = "", var respuesta: String = "",var correo:String="") {
    override fun toString() = "$id_pregunta  $respuesta  $correo"
}