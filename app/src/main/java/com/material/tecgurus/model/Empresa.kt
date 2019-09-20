package com.material.tecgurus.model

import android.support.annotation.Keep
import java.io.Serializable

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Empresa(private var nombre: String = "",
private var correo: String = "",
private var telefono: String = "",
private var direccion: String = "",//direccion,edad,telefono campos nuevos
private var foto: String = "",
private var giro: String = "",
private var id_empresa: String = "",
private var estatus: String = "",
private var token: String = "",
private var fecha_vencimiento_plan: String = "",
private var fecha_registro: String = "",
private var uid: String = "") {

 */
@Keep
class Empresa(public var nombre: String = "",
              public var correo: String = "",
              public var telefono: String = "",
              public var direccion: String = "",//direccion,edad,telefono campos nuevos
              public var foto: String = "",
              public var giro: String = "",
              public var id_empresa: String = "",
              public var estatus: String = "",
              public var token: String = "",
              public var fecha_vencimiento_plan: String = "",
              public var fecha_registro: String = "",
              public var uid: String = "") {

    override fun toString() = "$nombre $fecha_vencimiento_plan $correo $telefono $direccion" +
            " $foto $giro $id_empresa $token $fecha_registro $estatus $uid"
}

/*
@Keep
class Empresa : Serializable {
    public var nombre: String = ""
    public var correo: String = ""
    public var telefono: String = ""
    public var direccion: String = ""//direccion,edad,telefono campos nuevos
    public var foto: String = ""
    public var giro: String = ""
    public var id_empresa: String = ""
    public var estatus: String = ""
    public var token: String = ""
    public var fecha_vencimiento_plan: String = ""
    public var fecha_registro: String = ""
    public var uid: String = ""

    constructor() {}
    constructor(nombre: String, correo: String, telefono: String, direccion: String, foto: String, giro: String, id_empresa: String, estatus: String, token: String, fecha_vencimiento_plan: String, fecha_registro: String, uid: String) {
        this.nombre = nombre
        this.correo = correo
        this.telefono = telefono
        this.direccion = direccion
        this.foto = foto
        this.giro = giro
        this.id_empresa = id_empresa
        this.estatus = estatus
        this.token = token
        this.fecha_vencimiento_plan = fecha_vencimiento_plan
        this.fecha_registro = fecha_registro
        this.uid = uid
    }


    override fun toString() = "$nombre $fecha_vencimiento_plan $correo $telefono $direccion" +
            " $foto $giro $id_empresa $token $fecha_registro $estatus $uid"
}*/
