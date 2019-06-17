package com.material.components.model

class Usuario(val email:String = "", val image:Int=0, val name:String = ""){

    override fun toString()= "$email  $name"
}