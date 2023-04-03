package com.example.appkotlindsm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlindsm.modelo.Employee
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
class EmployeeSalaryActivity : AppCompatActivity() {


    private lateinit var adapter: Adapter_employee
    private lateinit var employeesList: MutableList<Employee>
    private lateinit var employeeRecyclerView: RecyclerView
    private lateinit var nameEditText: EditText
    private lateinit var salaryEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_salary)



    }

    fun getEmployees(){

    }
    fun createEmployee(nombre: String, salario: Double?) {

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





