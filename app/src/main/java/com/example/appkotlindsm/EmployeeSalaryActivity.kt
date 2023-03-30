package com.example.appkotlindsm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

class EmployeeSalaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_salary)
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