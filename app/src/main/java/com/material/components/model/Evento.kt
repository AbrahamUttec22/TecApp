package com.material.components.model

class Evento(var description:String = "", var fecha:String="", var image:String = "", var titulo:String="", var ubicacion:String=""){
    override fun toString()= "$description  $fecha $image $titulo $ubicacion"
}