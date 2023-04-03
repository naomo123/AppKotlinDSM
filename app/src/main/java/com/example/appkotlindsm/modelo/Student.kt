package com.example.appkotlindsm.modelo
import com.google.firebase.database.DataSnapshot

data class Student(
    var id: String = "",
    var name: String = "",
    var grade1: Double = 0.0,
    var grade2: Double = 0.0,
    var grade3: Double = 0.0,
    var documentId: String? = null
) {
    companion object {
        const val CHILD_NAME = "estudiantes"
        fun fromSnapshot(snapshot: DataSnapshot): Student {
            val student = snapshot.getValue(Student::class.java)!!
            student.id = snapshot.key ?: ""
            student.documentId = snapshot.key ?: ""
            return student
        }
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "grade1" to grade1,
            "grade2" to grade2,
            "grade3" to grade3,
        )
    }
}
