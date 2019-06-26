package com.material.components.model

class Anuncio (var description:String = "", var titulo:String = "",var fecha:String=""){

    override fun toString()= "$description  $titulo $fecha"
}