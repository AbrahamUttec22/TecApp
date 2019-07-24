package com.material.components.model

class Usuario(var email:String = "",
              var ubicacion:String="",
              var name:String = "",
              var rol:String="",//direccion,edad,telefono campos nuevos
              var direccion:String="",
              var edad:String="",
              var telefono:String="",
              var id:String="",
              var id_empresa:String="",
              var token:String=""){

    override fun toString()= "$email  $name $ubicacion $rol $direccion $edad $telefono $id $id_empresa $token"
}