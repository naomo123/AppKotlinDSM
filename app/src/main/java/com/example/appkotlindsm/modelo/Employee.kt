package com.example.appkotlindsm.modelo
import com.google.firebase.database.DataSnapshot

data class Employee(
    var id: String = "",
    var nombre: String = "",
    var salario: Double = 0.0,
) {


    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "salario" to salario
        )
    }
}