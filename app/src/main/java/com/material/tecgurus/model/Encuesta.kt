package com.material.tecgurus.model

class Encuesta(var pregunta: String = "",
               var respuestas: List<String>? = null,
               var status:String="", var id_empresa:String="") {
    override fun toString() = "$pregunta  $respuestas $status $id_empresa"
}