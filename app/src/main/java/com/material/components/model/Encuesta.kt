package com.material.components.model

class Encuesta(var pregunta: String = "", var respuestas: List<String>? = null,var status:String="") {
    override fun toString() = "$pregunta  $respuestas $status "
}