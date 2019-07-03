package com.material.components.model

class Encuesta(var pregunta: String = "", var respuestas: List<String>? = null) {
    override fun toString() = "$pregunta  $respuestas"
}