package com.example.appkotlindsm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlindsm.modelo.Employee


class Adapter_employee(private val employees: MutableList<Employee>) :
    RecyclerView.Adapter<Adapter_employee.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.item_Name)
        val salaryTextView: TextView = itemView.findViewById(R.id.item_salaryb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employees[position]
        holder.nameTextView.text = employee.nombre
        holder.salaryTextView.text = employee.salario.toString()
    }

    override fun getItemCount(): Int {
        return employees.size
    }
}
