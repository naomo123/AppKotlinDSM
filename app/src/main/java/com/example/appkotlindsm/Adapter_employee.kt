package com.example.appkotlindsm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlindsm.modelo.Employee
import java.text.DecimalFormat


class Adapter_employee(private val employees: MutableList<Employee>) :
    RecyclerView.Adapter<Adapter_employee.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.item_Name)
        val tvSalBase: TextView = itemView.findViewById(R.id.item_salaryb)
        val tvSalNeto: TextView = itemView.findViewById(R.id.item_salaryn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employees[position]

        holder.tvNombre.text = "Nombre: " + employee.nombre
        holder.tvSalBase.text = "Salario base: $" + employee.salario

        val resultado= (employee.salario.toDouble())-(employee.salario.toDouble()*0.03)-(employee.salario.toDouble()*0.04)-(employee.salario.toDouble()*0.05)
        val formatoDecimal = DecimalFormat("0.00")
        val resultadoRedondeado = formatoDecimal.format(resultado)

        holder.tvSalNeto.text = "Salario neto: $" + resultadoRedondeado
    }

    override fun getItemCount(): Int {
        return employees.size
    }
}
