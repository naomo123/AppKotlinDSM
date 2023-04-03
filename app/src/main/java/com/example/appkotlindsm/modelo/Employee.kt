package com.example.appkotlindsm.modelo
import com.google.firebase.database.DataSnapshot

data class Employee(
    var id: String = "",
    var nombre: String = "",
    var salario: Double = 0.0,
    var documentId: String? = null
) {
    companion object {
        const val CHILD_NAME = "empleados"
        fun fromSnapshot(snapshot: DataSnapshot): Employee {
            val empleado = snapshot.getValue(Employee::class.java)!!
            empleado.id = snapshot.key ?: ""
            empleado.documentId = snapshot.key ?: ""
            return empleado
        }
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "salario" to salario
        )
    }
}
