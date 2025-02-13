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
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // Ajuste para alinhar corretamente

        val days = mutableListOf<DayStatus?>()

        // Adiciona os cabeçalhos dos dias da semana (DOM, SEG, TER...)
        for (i in 0..6) {
            days.add(DayStatus(-1, false)) // Código especial para cabeçalho
        }

        // Adiciona espaços vazios no início para alinhar o primeiro dia
        for (i in 0 until firstDayOfWeek) {
            days.add(null)
        }

        // Adiciona os dias reais do mês
        for (day in 1..daysInMonth) {
            days.add(DayStatus(day, Random().nextBoolean()))
        }

        return days
    }
}
