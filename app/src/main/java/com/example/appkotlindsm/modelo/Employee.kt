package com.example.appkotlindsm.modelo

import com.google.firebase.firestore.DocumentSnapshot

class Employee(
    var nombre: String = "",
    var salario: Double = 0.0
) {
    companion object {
        const val COLLECTION_NAME = "empleados"

        fun fromSnapshot(snapshot: DocumentSnapshot): Employee {
            val empleado = snapshot.toObject(Employee::class.java)!!
            empleado.documentId = snapshot.id
            return empleado
        }
    }

    var documentId: String? = null

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "salario" to salario
        )
    }
}
