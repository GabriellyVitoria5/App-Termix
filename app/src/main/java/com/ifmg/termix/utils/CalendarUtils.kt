package com.ifmg.termix.utils

import com.ifmg.termix.model.CalendarDay
import java.util.*

object CalendarUtils {
    fun generateMonthData(year: Int, month: Int): List<CalendarDay> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val monthData = mutableListOf<CalendarDay>()

        for (day in 1..daysInMonth) {
            val played = (0..1).random() == 1 // TODO: Substituir por dados do banco
            val won = if (played) (0..1).random() == 1 else null

            monthData.add(CalendarDay(day, played, won))
        }
        return monthData
    }
}
