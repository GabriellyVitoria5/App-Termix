package com.ifmg.termix.model

data class CalendarDay(
    val day: Int,
    val played: Boolean, // Se jogou nesse dia
    val won: Boolean? // Se ganhou ou perdeu (null significa que não jogou)
)
