package com.example.appkotlindsm

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appkotlindsm.modelo.Student
import java.text.DecimalFormat


class Adapter_student(private val students: MutableList<Student>,
                      private val onItemEditClickListener: (Student) -> Unit,
                      private val onItemDeleteClickListener: (Student) -> Unit) :
    RecyclerView.Adapter<Adapter_student.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val llStudentInfo: LinearLayout = itemView.findViewById(R.id.llEstudiante)
        val tvName: TextView = itemView.findViewById(R.id.item_Student)
        val tvGrade1: TextView = itemView.findViewById(R.id.item_grade1)
        val tvGrade2: TextView = itemView.findViewById(R.id.item_grade2)
        val tvGrade3: TextView = itemView.findViewById(R.id.item_grade3)
        val tvAverage: TextView = itemView.findViewById(R.id.item_average)
        val tvState: TextView = itemView.findViewById(R.id.item_state)
        val btnEdit: Button = itemView.findViewById(R.id.btn_edit)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grade, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = students[position]

        holder.tvName.text = "Nombre: " + student.name
        holder.tvGrade1.text = "Nota 1: " + student.grade1
        holder.tvGrade2.text = "Nota 2: " + student.grade2
        holder.tvGrade3.text = "Nota 3: " + student.grade3
        val average = (student.grade1 + student.grade2 + student.grade3) / 3
        holder.tvAverage.text = "Promedio: " + DecimalFormat("0.00").format(average)
        holder.tvState.text = if(average < 6) "Reprobado" else "Aprobado"
        holder.tvState.setTextColor(if(average < 6) Color.RED else Color.GREEN)

        holder.btnEdit.setOnClickListener {
            onItemEditClickListener(student)
        }
        holder.btnDelete.setOnClickListener {
            onItemDeleteClickListener(student)
        }
    }

    override fun getItemCount(): Int {
        return students .size
    }
}
