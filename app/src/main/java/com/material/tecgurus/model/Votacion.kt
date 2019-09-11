package com.material.tecgurus.model

class Votacion(var id_pregunta: String = "", var respuesta: String = "",
               var correo:String="",var id_empresa:String="") {
    override fun toString() = "$id_pregunta  $respuesta  $correo $id_empresa"
}