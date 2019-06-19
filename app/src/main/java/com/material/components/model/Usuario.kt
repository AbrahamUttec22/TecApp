package com.material.components.model

class Usuario(var email:String = "", var image:Int=0, var name:String = "", var rol:String=""){

    override fun toString()= "$email  $name $image $rol"
}