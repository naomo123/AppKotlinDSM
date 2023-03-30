package com.example.appkotlindsm

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private var emailTV: EditText? = null
    private var passwordTV: EditText? = null
    private var singup: Button? = null
    private var progressBar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        mAuth= FirebaseAuth.getInstance()
        initializeUI()
        singup!!.setOnClickListener { registerNewUser() }
    }
    private fun registerNewUser(){
        progressBar!!.visibility= View.VISIBLE
        val email:String
        val psswd:String
        email=emailTV!!.text.toString()
        psswd=passwordTV!!.text.toString()
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Por favor ingresa un correo", Toast.LENGTH_LONG).show()

            return
        }
        if(TextUtils.isEmpty(psswd)){
            Toast.makeText(this, "Por favor ingresa una contraseña", Toast.LENGTH_LONG).show()

            return
        }
        mAuth!!.createUserWithEmailAndPassword(email, psswd).addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                Toast.makeText(  this, "Registro satisfactorio",Toast.LENGTH_LONG).show()
                Log.d(TAG, "User locale updated successfully")
                progressBar!!.setVisibility(View.GONE)
                val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
                startActivity(intent)

            }
            else{
                Toast.makeText(  this, "El registro falló, intente más tarde",Toast.LENGTH_LONG).show()
                Log.e(TAG, "Error updating user locale: ${task.exception}")
                progressBar!!.setVisibility(View.GONE)


            }
        }

    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        singup = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBar)
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