package com.material.components.model

/**
 * @author Abraham
 */
class Empresa(var nombre:String = "",
              var correo:String="",
              var telefono:String = "",
              var direccion:String="",//direccion,edad,telefono campos nuevos
              var foto:String="",
              var giro:String="",
              var id_empresa:String="",
              var token:String="",
              var uid:String=""){

    override fun toString()= "$nombre  $correo $telefono $direccion" +
            " $foto $giro $id_empresa $token"
}