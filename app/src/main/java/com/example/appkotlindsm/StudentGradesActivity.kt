package com.example.appkotlindsm

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlindsm.modelo.Employee
import com.example.appkotlindsm.modelo.Error
import com.example.appkotlindsm.modelo.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.lang.StringBuilder

class StudentGradesActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var listStudents: MutableList<Student>
    private lateinit var adapterStudent: Adapter_student
    private lateinit var recyclerView: RecyclerView

    private lateinit var etName: EditText
    private lateinit var etGrade1: EditText
    private lateinit var etGrade2: EditText
    private lateinit var etGrade3: EditText
    private lateinit var addBtn: FloatingActionButton
    private lateinit var editBtn: FloatingActionButton
    private var isEditMode = false
    private var selectedStudent: Student? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_grades)

        // Inicializar database
        database = FirebaseDatabase.getInstance().getReference("students")

        // Inicializar vistas
        etName = findViewById(R.id.et_name)
        etGrade1 = findViewById(R.id.et_grade1)
        etGrade2 = findViewById(R.id.et_grade2)
        etGrade3 = findViewById(R.id.et_grade3)

        recyclerView = findViewById(R.id.RVGrades)

        addBtn = findViewById<FloatingActionButton>(R.id.add_btn)
        editBtn = findViewById<FloatingActionButton>(R.id.edit_btn)
        editBtn.isVisible = false

        listStudents = mutableListOf()
        adapterStudent = Adapter_student(listStudents, onItemEditClickListener = { student ->
            selectedStudent = student
            etName.setText(student.name)
            etGrade1.setText(student.grade1.toString())
            etGrade2.setText(student.grade2.toString())
            etGrade3.setText(student.grade3.toString())
            isEditMode = true
            addBtn.isVisible = false
            editBtn.isVisible = true
        }, onItemDeleteClickListener = { student ->
            selectedStudent = student
            Log.e("Student", student.toString())
            val builder = AlertDialog.Builder(this)
            builder.setMessage("¿Estás seguro de querer eliminar?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    database.child(student.id).removeValue()
                    adapterStudent.notifyDataSetChanged()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        })


        recyclerView.adapter = adapterStudent
        recyclerView.layoutManager = LinearLayoutManager(this)

        getStudents()

        addBtn.setOnClickListener {
            //agregar
            val name = etName.text.toString()
            val grade1 = etGrade1.text.toString().toDoubleOrNull()
            val grade2 = etGrade2.text.toString().toDoubleOrNull()
            val grade3 = etGrade3.text.toString().toDoubleOrNull()
            createStudent(name, grade1, grade2, grade3)
        }

        editBtn.setOnClickListener{
            val name = etName.text.toString()
            val grade1 = etGrade1.text.toString().toDoubleOrNull()
            val grade2 = etGrade2.text.toString().toDoubleOrNull()
            val grade3 = etGrade3.text.toString().toDoubleOrNull()
            if(editStudent(name, grade1, grade2, grade3))
            {
                addBtn.isVisible = true
                editBtn.isVisible = false
            }
        }
    }

    fun getStudents(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listStudents.clear()
                val studentObject = snapshot.children
                for(studentSnapshot in studentObject){
                    val students = studentSnapshot.getValue(Student::class.java)
                    students?.let {
                        it.id = studentSnapshot.key ?: ""
                        it.documentId = studentSnapshot.key ?: ""
                        listStudents.add(it)
                        Log.e("Student", it.toString())
                    }
                }
                adapterStudent.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StudentGradesActivity, "Error al obtener lista de estudiantes", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun createStudent(name: String, grade1: Double?, grade2: Double?, grade3: Double?) {
        var flag = validateInputs(name, grade1, grade2, grade3)
        if(!flag){
            return
        }

        // Crear un nuevo objeto Employee con los datos proporcionados
        val newStudent = Student(name = name, grade1 = grade1?: 0.00, grade2 = grade2?: 0.00, grade3 = grade3?: 0.00)

        // Escribir los datos del nuevo empleado en la base de datos
        val newStudentReference = database.push()
        newStudentReference.setValue(newStudent.toMap())

        // Mostrar un mensaje de éxito al usuario
        Toast.makeText(this, "Estudainte agregado correctamente", Toast.LENGTH_SHORT).show()

        etName.setText("")
        etGrade1.setText("")
        etGrade2.setText("")
        etGrade3.setText("")
        isEditMode = false
        selectedStudent = null
    }
    fun editStudent(name: String, grade1: Double?, grade2: Double?, grade3: Double?): Boolean {
        if(selectedStudent == null){
            etName.setText("")
            etGrade1.setText("")
            etGrade2.setText("")
            etGrade3.setText("")
            isEditMode = false
            selectedStudent = null
            return false
        }
        var flag = validateInputs(name, grade1, grade2, grade3)
        if(!flag){
            return false
        }
        var student = Student(name = name, grade1 = grade1?: 0.0, grade2 = grade2?: 0.0, grade3 = grade3?: 0.0)
        database.child(selectedStudent!!.id).setValue(student.toMap())
        adapterStudent.notifyDataSetChanged()


        // Mostrar un mensaje de éxito al usuario
        Toast.makeText(this, "Estudiante modificado correctamente", Toast.LENGTH_SHORT).show()

        etName.setText("")
        etGrade1.setText("")
        etGrade2.setText("")
        etGrade3.setText("")
        isEditMode = false
        selectedStudent = null
        return true;
    }

    fun validateInputs(name: String, grade1: Double?, grade2: Double?, grade3: Double?): Boolean{
        var errors = mutableListOf<com.example.appkotlindsm.modelo.Error>()
        if(name.isNullOrEmpty())
            setError("name", "Debe ingresar un nombre", errors)
        if(grade1 == null)
            setError("grade1", "Debe ingresar un nota 1 válida", errors)
        if(grade2 == null)
            setError("grade2", "Debe ingresar un nota 2 válida", errors)
        if(grade3 == null)
            setError("grade3", "Debe ingresar un nota 3 válida", errors)

        if(grade1 != null && (grade1 < 0 || grade1 > 10))
            setError("grade1", "Debe ingresar una nota entre 0 y 10", errors)
        if(grade2 != null && (grade2 < 0 || grade2 > 10))
            setError("grade2", "Debe ingresar una nota entre 0 y 10", errors)
        if(grade3 != null && (grade3 < 0 || grade3 > 10))
            setError("grade3", "Debe ingresar una nota entre 0 y 10", errors)

        if(errors.size > 0){
            for (error in errors){
                when(error.key){
                    "name" -> etName.error = formatErrorDescription(error.errors)
                    "grade1" -> etGrade1.error = formatErrorDescription(error.errors)
                    "grade2" -> etGrade2.error = formatErrorDescription(error.errors)
                    "grade3" -> etGrade3.error = formatErrorDescription(error.errors)
                }
            }
            Toast.makeText(this, "Se han encontrado errores de validación", Toast.LENGTH_LONG).show()
            return false
        }
        else{
            etName.error = null
            etGrade1.error = null
            etGrade2.error = null
            etGrade3.error = null
        }
        return true
    }

    fun setError(key: String, errorDescription: String, errorsReference: MutableList<com.example.appkotlindsm.modelo.Error>){
        var existsKey = errorsReference.find { e -> e.key == key }
        if(existsKey == null){
            errorsReference.add(com.example.appkotlindsm.modelo.Error(key, mutableListOf<String>(errorDescription)))
        }
        else{
            var existsError = existsKey.errors.find { e -> e.lowercase() == errorDescription.lowercase() }
            if(existsError == null){
                existsKey.errors.add(errorDescription)
            }
        }
    }
    fun formatErrorDescription(errors: MutableList<String>): String{
        var str: java.lang.StringBuilder = StringBuilder()
        for (error in errors){
            str.appendLine("- $error")
        }
        return str.toString()
    }


    /** Menu creation and actions */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id== R.id.op1)
        {
            Toast.makeText(this, "Cálculo de promedio de un estudiante", Toast.LENGTH_LONG).show()
            val intent=
                Intent(this,StudentGradesActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        if(id== R.id.op2)
        {
            Toast.makeText(this, "Salario neto de un empleado", Toast.LENGTH_LONG).show()
            val intent=
                Intent(this,EmployeeSalaryActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}