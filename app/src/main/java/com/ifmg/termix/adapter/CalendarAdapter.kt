package com.ifmg.termix.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ifmg.termix.R
import com.ifmg.termix.model.CalendarDay

class CalendarAdapter(private val days: List<CalendarDay>) :
    RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayText: TextView = view.findViewById(R.id.dayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]

        holder.dayText.text = day.day.toString()
        when {
            day.won == true -> holder.dayText.setBackgroundColor(Color.GREEN) // Vitória
            day.won == false -> holder.dayText.setBackgroundColor(Color.RED) // Derrota
            else -> holder.dayText.setBackgroundColor(Color.TRANSPARENT) // Não jogado
        }
    }

    override fun getItemCount() = days.size
}
