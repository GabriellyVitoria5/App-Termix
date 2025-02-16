package com.ifmg.termix.utils

import com.ifmg.termix.adapter.DayStatus
import java.util.*

object CalendarUtils {
    fun generateMonthData(year: Int, month: Int): List<DayStatus?> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1 = Domingo, 2 = Segunda, ..., 7 = Sábado

        val days = mutableListOf<DayStatus?>()

        // Adiciona os cabeçalhos dos dias da semana (S, T, Q, Q, S, S, D)
        val weekDays = listOf("S", "T", "Q", "Q", "S", "S", "D")

        // Adiciona os dias da semana ao início do calendário
        for (i in 0..6) {
            days.add(DayStatus(-1, false)) // Código especial para cabeçalhos
        }

        // Ajusta os espaços vazios antes do primeiro dia do mês
        val adjustedFirstDay = if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2
        for (i in 0 until adjustedFirstDay) {
            days.add(null)
        }

        // Adiciona os dias reais do mês
        for (day in 1..daysInMonth) {
            days.add(DayStatus(day, Random().nextBoolean()))
        }

        return days
    }
}
