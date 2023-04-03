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
import androidx.recyclerview.widget.ItemTouchHelper
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
        recyclerView = findViewById(R.id.RVempleados)
        // Inicializar database
        database = FirebaseDatabase.getInstance().getReference("employees")

        // Inicializar vistas
        etNombre = findViewById(R.id.et_name)
        etSalario = findViewById(R.id.et_salary)

        listemployees = mutableListOf()



        // Obtener lista de empleados de Firebase Database
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
                        // Añadir el objeto empleado a la lista
                        listemployees.add(it)
                    }
                }

                // Inicializar lista de empleados y adaptador
                adapterEmployee = Adapter_employee(listemployees, onItemClickListener = { employee ->
                    // Al hacer click largo en un item, cambiar a modo edición
                    selectedEmployee = employee
                    etNombre.setText(employee.nombre)
                    etSalario.setText(employee.salario.toString())
                    isEditMode = true
                })


                // Configurar RecyclerView

                recyclerView.adapter = adapterEmployee
                recyclerView.layoutManager = LinearLayoutManager(this@EmployeeSalaryActivity)

                // Notificar al adaptador que la lista ha cambiado
                adapterEmployee.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error
                Toast.makeText(this@EmployeeSalaryActivity, "Error al obtener lista de empleados", Toast.LENGTH_SHORT).show()
            }
        })


        val agregar: FloatingActionButton = findViewById<FloatingActionButton>(R.id.calc_btn2)
        agregar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val salario = etSalario.text.toString().toDoubleOrNull()
            if (isEditMode) {
                // Actualizar empleado existente
                selectedEmployee?.let { employee ->
                    employee.nombre = nombre
                    employee.salario = salario ?: 0.0
                    updateEmployee(employee)
                }
            } else {
                // Agregar nuevo empleado
                createEmployee(nombre, salario)
            }

            // Limpiar los campos de texto y desactivar el modo edición
            etNombre.setText("")
            etSalario.setText("")
            isEditMode = false
            selectedEmployee = null

        }

        //eliminar
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Eliminar el elemento seleccionado de la base de datos
                listemployees.get(viewHolder.adapterPosition).id?.let { database.child(it).setValue(null) }

                // Eliminar el elemento de la lista y notificar al adaptador
                listemployees.removeAt(viewHolder.adapterPosition)
                adapterEmployee.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    fun createEmployee(nombre: String, salario: Double?) {
        // Validar que se haya ingresado un nombre y un salario
        if (nombre.isBlank() || salario == null) {
            Toast.makeText(this, "Debe ingresar un nombre y un salario válido", Toast.LENGTH_SHORT).show()
            return
        }



        // Crear un nuevo objeto Employee con los datos proporcionados
        val newEmployee = Employee(nombre = nombre, salario = salario)
        Log.d(TAG, newEmployee.toString())
        // Escribir los datos del nuevo empleado en la base de datos

        val newEmployeeReference = database.push()
        newEmployeeReference.setValue(newEmployee.toMap())

        // Mostrar un mensaje de éxito al usuario
        Toast.makeText(this, "Empleado agregado correctamente", Toast.LENGTH_SHORT).show()

        // Limpiar los campos de texto y desactivar el modo edición
        etNombre.setText("")
        etSalario.setText("")
        isEditMode = false
        selectedEmployee = null
    }

    private fun updateEmployee(employee: Employee) {
        // Validar que se haya ingresado un nombre y un salario
        if (employee.nombre.isBlank() || employee.salario == null) {
            Toast.makeText(this, "Debe ingresar un nombre y un salario válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Actualizar los datos del empleado en la base de datos
        database.child(employee.id).setValue(employee.toMap())

        // Mostrar un mensaje de éxito al usuario
        Toast.makeText(this, "Empleado actualizado correctamente", Toast.LENGTH_SHORT).show()

        // Notificar al adaptador que la lista ha cambiado
        adapterEmployee.notifyDataSetChanged()
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





