package com.material.tecgurus.model

class ImagenProyecto (var name:String = "",var ubicacion:String = "",
                      var id_empresa:String = ""){

    override fun toString()= "$name $ubicacion $id_empresa"
}