package com.material.components.model

class Anuncio (var description:String = "", var titulo:String = "",var fecha:String="",var ubicacion:String=""){

    override fun toString()= "$description  $titulo $fecha $ubicacion"
}