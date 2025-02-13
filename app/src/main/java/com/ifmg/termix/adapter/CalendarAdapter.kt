package com.ifmg.termix.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ifmg.termix.R
import java.util.Locale

class CalendarAdapter(private val days: List<DayStatus?>) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val weekDays = listOf("D", "S", "T", "Q", "Q", "S", "S") // Apenas a primeira letra

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayStatus = days[position]

        if (position < 7) { // Cabeçalhos dos dias da semana
            holder.dayTextView.text = weekDays[position]
            holder.dayContainer.setCardBackgroundColor(Color.WHITE) // Fundo branco
            holder.dayTextView.setTextColor(Color.BLACK) // Texto preto
            holder.dayContainer.visibility = View.VISIBLE
        } else if (dayStatus == null) {
            // Oculta os espaços vazios antes do primeiro dia do mês
            holder.dayContainer.visibility = View.INVISIBLE
        } else {
            holder.dayTextView.text = String.format(Locale.getDefault(), "%d", dayStatus.day)
            holder.dayContainer.visibility = View.VISIBLE

            // Se o dia não foi jogado
            if (dayStatus.day == 0) {
                holder.dayContainer.setCardBackgroundColor(Color.WHITE) // Branco para dias não jogados
            }
            // Se o dia foi jogado e a palavra foi acertada
            else if (dayStatus.acertou) {
                holder.dayContainer.setCardBackgroundColor(Color.parseColor("#A8D5BA")) // Verde pastel para palavra acertada
            }
            // Se o dia foi jogado e a palavra foi errada
            else {
                holder.dayContainer.setCardBackgroundColor(Color.parseColor("#F8B8B8")) // Vermelho pastel para palavra errada
            }
        }
    }

    override fun getItemCount(): Int = days.size

    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.dayTextView)
        val dayContainer: CardView = view.findViewById(R.id.dayContainer)
    }
}


data class DayStatus(val day: Int, val acertou: Boolean)