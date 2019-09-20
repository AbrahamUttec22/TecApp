package com.material.tecgurus.model

import android.support.annotation.Keep

/**
 * To set private is very important because firebase not deserializable on cloud firestore
 * @author Abraham Casas Aguilar
 * class Actividades(private var actividad: String = "",
private var descripcion: String = "",
private var correo: String = "",
private var estatus: String = "",
private var id: String = "",
private var id_usuario: String = "",
private var id_empresa: String = "",
private var fecha_compromiso: String = "",
private var email_asigno: String = ""
 */
@Keep
class Actividades(var actividad: String = "",
                  var descripcion: String = "",
                  var correo: String = "",
                  var estatus: String = "",
                  var id: String = "",
                  var id_usuario: String = "",
                  var id_empresa: String = "",
                  var fecha_compromiso: String = "",
                  var email_asigno: String = ""

        // var fecha_hora_asignada: String = "",
        //var fecha_hora_terminada: String = ""
) {

    override fun toString() = "$actividad  $correo $estatus $id $id_usuario $id_empresa $fecha_compromiso $descripcion $email_asigno"
}