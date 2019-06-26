package com.material.components.model

class Usuario(var email:String = "",
              var image:Int=0,
              var name:String = "",
              var rol:String="",//direccion,edad,telefono campos nuevos
              var direccion:String="",
              var edad:String="",
              var telefono:String=""){

    override fun toString()= "$email  $name $image $rol $direccion $edad $telefono"
}