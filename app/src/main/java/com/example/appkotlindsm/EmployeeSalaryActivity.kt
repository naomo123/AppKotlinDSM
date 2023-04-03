package com.example.appkotlindsm

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlindsm.modelo.Employee
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class EmployeeSalaryActivity : AppCompatActivity() {


    private lateinit var database: DatabaseReference
    private lateinit var listemployees: MutableList<Employee>
    private lateinit var adapterEmployee: Adapter_employee
    private lateinit var recyclerView: RecyclerView

    private lateinit var etNombre: EditText
    private lateinit var etSalario: EditText
    private var isEditMode = false
    private var selectedEmployee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_salary)

        // Inicializar database
        database = FirebaseDatabase.getInstance().getReference("employees")

        // Inicializar vistas
        etNombre = findViewById(R.id.et_name)
        etSalario = findViewById(R.id.et_salary)
        recyclerView = findViewById(R.id.RVempleados)

        val agregar: FloatingActionButton = findViewById<FloatingActionButton>(R.id.calc_btn2)

        // Inicializar lista de empleados y adaptador
        listemployees = mutableListOf()
        adapterEmployee = Adapter_employee(listemployees, onItemClickListener = { employee ->
            // Al hacer click largo en un item, cambiar a modo edición
            selectedEmployee = employee
            etNombre.setText(employee.nombre)
            etSalario.setText(employee.salario.toString())
            isEditMode = true
        })


        // Configurar RecyclerView
        recyclerView.adapter = adapterEmployee
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener lista de empleados de Firebase Database y actualizar RecyclerView
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Limpiar la lista de empleados existentes
                listemployees.clear()

                // Recorrer los hijos de la referencia y obtener los datos de los empleados
                for (employeeSnapshot in snapshot.children) {
                    val employee = employeeSnapshot.getValue(Employee::class.java)
                    employee?.let {
                        // Asignar el ID del documento al objeto empleado
                        it.id = employeeSnapshot.key ?: ""
                        it.documentId = employeeSnapshot.key ?: ""
                        // Añadir el objeto empleado a la lista
                        listemployees.add(it)
                    }
                }

                // Notificar al adaptador que la lista ha cambiado
                adapterEmployee.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error
                Toast.makeText(this@EmployeeSalaryActivity, "Error al obtener lista de empleados", Toast.LENGTH_SHORT).show()
            }
        })
        getEmployees()
        //agregar
        val nombre = etNombre.text.toString()
        val salario = etSalario.text.toString().toDoubleOrNull()
        agregar.setOnClickListener {

            createEmployee(nombre, salario)
            Log.d(TAG, "al menos si hace algo")
            Log.d(TAG, etNombre.text.toString())
            Log.d(TAG, etSalario.text.toString())  }
    }

    fun getEmployees(){
        database.child(Employee.CHILD_NAME).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listemployees.clear()
                for (employeeSnapshot in snapshot.children) {
                    val employee = Employee.fromSnapshot(employeeSnapshot)
                    listemployees.add(employee)
                }
                adapterEmployee.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error al leer los datos de Firebase", error.toException())
            }
        })
    }
    fun createEmployee(nombre: String, salario: Double?) {
        // Crear un nuevo objeto Employee con los datos proporcionados
        val newEmployee = Employee(nombre = nombre, salario = salario ?: 0.0)

        // Escribir los datos del nuevo empleado en la base de datos
        val newEmployeeReference = database.child(Employee.CHILD_NAME).push()
        newEmployeeReference.setValue(newEmployee.toMap())

        // Mostrar un mensaje de éxito al usuario
        Toast.makeText(this, "Empleado agregado correctamente", Toast.LENGTH_SHORT).show()

        // Limpiar los campos de texto y desactivar el modo edición
        etNombre.setText("")
        etSalario.setText("")
        isEditMode = false
        selectedEmployee = null
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
        if(id== R.id.inicio)
        {
            Toast.makeText(this, "Inicio", Toast.LENGTH_LONG).show()
            val intent=Intent(this,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}





